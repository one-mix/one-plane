package com.oneplane.post.service;

import com.oneplane.post.dao.PostDao;
import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostListResponse;
import com.oneplane.post.domain.PostSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostDao postDao;

    /**
     * 게시글 전체 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        log.info("게시글 전체 목록 조회 요청");

        try {
            List<Post> posts = postDao.findAllPosts();
            posts.forEach(this::processPostData);
            return posts;
        } catch (Exception e) {
            throw new RuntimeException("게시글 목록을 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 페이징된 게시글 목록 조회
     */
    @Transactional(readOnly = true)
    public PostListResponse getPostsWithPaging(PostSearchCondition condition) {
        try {
            // 기본값 설정
            condition.setDefaults();

            // 총 게시글 수 조회
            int totalElements = postDao.countPosts(condition);

            // 페이징된 게시글 목록 조회
            List<Post> posts = postDao.findPostsWithPaging(condition);

            // 게시글 데이터 후처리
            posts.forEach(this::processPostData);

            // 페이지 정보 계산
            int totalPages = (int) Math.ceil((double) totalElements / condition.getSize());
            boolean hasNext = condition.getPage() < totalPages;
            boolean hasPrevious = condition.getPage() > 1;

            PostListResponse response = PostListResponse.builder()
                    .posts(posts)
                    .currentPage(condition.getPage())
                    .totalPages(totalPages)
                    .totalElements(totalElements)
                    .size(condition.getSize())
                    .hasNext(hasNext)
                    .hasPrevious(hasPrevious)
                    .build();

            return response;

        } catch (Exception e) {
            log.error("페이징된 게시글 목록 조회 중 오류 발생", e);
            throw new RuntimeException("게시글 목록을 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 카테고리별 게시글 목록 조회
     */
    @Transactional(readOnly = true)
    public PostListResponse getPostsByCategory(String category, int page, int size) {

        try {
            // 페이지 유효성 검사
            if (page < 1) page = 1;
            if (size < 1) size = 6;

            int offset = (page - 1) * size;

            // 총 게시글 수 조회
            int totalElements = postDao.countPostsByCategory(category);

            // 페이징된 게시글 목록 조회
            List<Post> posts = postDao.findPostsByCategory(category, offset, size);

            // 게시글 데이터 후처리
            posts.forEach(this::processPostData);

            // 페이지 정보 계산
            int totalPages = (int) Math.ceil((double) totalElements / size);
            boolean hasNext = page < totalPages;
            boolean hasPrevious = page > 1;

            PostListResponse response = PostListResponse.builder()
                    .posts(posts)
                    .currentPage(page)
                    .totalPages(totalPages)
                    .totalElements(totalElements)
                    .size(size)
                    .hasNext(hasNext)
                    .hasPrevious(hasPrevious)
                    .build();

            return response;

        } catch (Exception e) {
            log.error("카테고리별 게시글 조회 중 오류 발생", e);
            throw new RuntimeException("게시글 목록을 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 게시글 데이터 후처리
     */
    private void processPostData(Post post) {
        // 내용이 너무 길면 요약
        if (post.getContent() != null && post.getContent().length() > 200) {
            String summary = extractTextFromHtml(post.getContent());
            if (summary.length() > 200) {
                summary = summary.substring(0, 200) + "...";
            }
        }

        // 썸네일 이미지가 없으면 본문에서 첫 번째 이미지 추출
        if (post.getThumbnailImage() == null || post.getThumbnailImage().isEmpty()) {
            String firstImage = extractFirstImageFromHtml(post.getContent());
            if (firstImage != null) {
                post.setThumbnailImage(firstImage);
            }
        }

        // 조회수, 좋아요 수 null 체크
        if (post.getViewCount() == null) {
            post.setViewCount(0);
        }
        if (post.getLikeCount() == null) {
            post.setLikeCount(0);
        }
        if (post.getCommentCount() == null) {
            post.setCommentCount(0);
        }
    }

    /**
     * HTML에서 텍스트만 추출
     */
    private String extractTextFromHtml(String html) {
        if (html == null) return "";

        return html.replaceAll("<[^>]*>", "")
                .replaceAll("&nbsp;", " ")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&")
                .trim();
    }

    /**
     * HTML에서 첫 번째 이미지 URL 추출
     */
    private String extractFirstImageFromHtml(String html) {
        if (html == null) return null;

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<img[^>]+src=[\"']([^\"']+)[\"'][^>]*>");
        java.util.regex.Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * 게시글 개수 조회
     */
    @Transactional(readOnly = true)
    public int getPostCount() {
        log.debug("게시글 총 개수 조회");
        List<Post> posts = postDao.findAllPosts();
        return posts.size();
    }

    /**
     * 검색 조건에 따른 게시글 개수 조회
     */
    @Transactional(readOnly = true)
    public int getPostCount(PostSearchCondition condition) {
        log.debug("검색 조건에 따른 게시글 개수 조회");
        return postDao.countPosts(condition);
    }

    /**
     * 게시글 작성
     */
    public Post createPost(Post post) {
        log.info("게시글 작성 처리 시작 - 제목: {}, 작성자: {}", post.getTitle(), post.getUserId());

        try {
            // 유효성 검사
            validatePost(post);

            // 썸네일 이미지 추출 (Summernote 내용에서 첫 번째 이미지)
            if (post.getThumbnailImage() == null || post.getThumbnailImage().isEmpty()) {
                String firstImage = extractFirstImageFromSummernote(post.getContent());
                if (firstImage != null) {
                    post.setThumbnailImage(firstImage);
                    log.info("썸네일 이미지 자동 추출: {}", firstImage);
                }
            }

            // 기본값 설정
            post.setViewCount(0);
            post.setLikeCount(0);
            post.setCommentCount(0);
            post.setCreatedAt(new Date());
            post.setUpdatedAt(new Date());

            int result = postDao.insertPost(post);
            if (result <= 0) {
                throw new RuntimeException("게시글 작성에 실패했습니다.");
            }

            log.info("게시글 작성 완료 - postId: {}, 제목: {}, 썸네일: {}",
                    post.getPostId(), post.getTitle(), post.getThumbnailImage());

            return post;

        } catch (Exception e) {
            log.error("게시글 작성 중 오류 발생", e);
            throw new RuntimeException("게시글 작성에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 게시글 유효성 검사
     */
    private void validatePost(Post post) {
        if (post.getUserId() == null) {
            throw new IllegalArgumentException("작성자 정보가 없습니다.");
        }

        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }

        if (post.getTitle().length() > 200) {
            throw new IllegalArgumentException("제목은 200자 이하로 입력해주세요.");
        }

        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }

        if (post.getCategory() == null) {
            throw new IllegalArgumentException("카테고리를 선택해주세요.");
        }
    }

    //Summernote HTML에서 첫 번째 이미지 URL 추출
    private String extractFirstImageFromSummernote(String html) {
        if (html == null || html.trim().isEmpty()) {
            return null;
        }

        try {
            // JSoup을 사용하여 HTML 파싱
            org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(html);
            org.jsoup.select.Elements imgElements = doc.select("img");

            if (!imgElements.isEmpty()) {
                org.jsoup.nodes.Element firstImg = imgElements.first();
                String src = firstImg.attr("src");

                if (src != null && !src.trim().isEmpty()) {
                    // 상대 경로인 경우 절대 경로로 변환할 수 있음
                    log.debug("첫 번째 이미지 URL 추출: {}", src);
                    return src;
                }
            }
        } catch (Exception e) {
            log.warn("이미지 URL 추출 중 오류 발생: {}", e.getMessage());
        }

        return null;
    }

}