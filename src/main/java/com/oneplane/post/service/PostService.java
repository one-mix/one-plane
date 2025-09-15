package com.oneplane.post.service;

import com.oneplane.country.service.CountryService;
import com.oneplane.post.dao.PostDao;
import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostListResponse;
import com.oneplane.post.domain.PostSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostDao postDao;
    private final CountryService countryService;

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

    /**
     * 게시글 상세 조회
     */
    @Transactional
    public Post getPostDetail(Integer postId) {
        log.info("게시글 상세 조회 및 조회수 증가 - postId: {}", postId);

        try {
            // 게시글 조회
            Post post = postDao.findPostById(postId);

            if (post == null) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }

            if (post.isDeleted()) {
                throw new IllegalArgumentException("삭제된 게시글입니다.");
            }

            // 조회수 증가
            postDao.increaseViewCount(postId);

            // 증가된 조회수 반영
            post.increaseViewCount();

            // 게시글 데이터 후처리
            processPostData(post);

            log.info("게시글 상세 조회 완료 - postId: {}, 제목: {}, 조회수: {}",
                    postId, post.getTitle(), post.getViewCount());

            return post;

        } catch (Exception e) {
            log.error("게시글 상세 조회 중 오류 발생 - postId: {}", postId, e);
            throw new RuntimeException("게시글을 불러오는데 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 게시글 조회
     */
    @Transactional(readOnly = true)
    public Post getPostById(Integer postId) {
        log.debug("게시글 조회 - postId: {}", postId);

        Post post = postDao.findPostById(postId);
        if (post != null) {
            processPostData(post);
        }

        return post;
    }

    /**
     * 조회수 증가
     */
    @Transactional
    public boolean increaseViewCount(Integer postId) {
        log.debug("조회수 증가 - postId: {}", postId);

        try {
            int result = postDao.increaseViewCount(postId);
            return result > 0;
        } catch (Exception e) {
            log.error("조회수 증가 중 오류 발생 - postId: {}", postId, e);
            return false;
        }
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public boolean deletePost(Integer postId, Integer userId) {
        log.info("게시글 삭제 처리 - postId: {}, 삭제자: {}", postId, userId);

        try {
            // 게시글 존재 확인
            Post post = postDao.findPostById(postId);
            if (post == null) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }

            if (post.isDeleted()) {
                throw new IllegalArgumentException("이미 삭제된 게시글입니다.");
            }

            // 삭제 처리
            int result = postDao.deletePost(postId);

            if (result > 0) {
                log.info("게시글 삭제 완료 - postId: {}, 제목: {}", postId, post.getTitle());
                return true;
            } else {
                log.warn("게시글 삭제 실패 - postId: {}", postId);
                return false;
            }

        } catch (Exception e) {
            log.error("게시글 삭제 중 오류 발생 - postId: {}", postId, e);
            throw new RuntimeException("게시글 삭제에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Post updatePost(Integer postId, Post updatePost) {
        log.info("게시글 수정 처리 - postId: {}", postId);

        try {
            // 기존 게시글 조회
            Post existingPost = postDao.findPostById(postId);
            if (existingPost == null) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }

            if (existingPost.isDeleted()) {
                throw new IllegalArgumentException("삭제된 게시글은 수정할 수 없습니다.");
            }

            // 수정할 데이터 설정
            existingPost.setTitle(updatePost.getTitle());
            existingPost.setContent(updatePost.getContent());
            existingPost.setCategory(updatePost.getCategory());
            existingPost.setCountryId(updatePost.getCountryId());
            existingPost.setUpdatedAt(new Date());

            // 썸네일 이미지 재추출
            if (updatePost.getThumbnailImage() == null || updatePost.getThumbnailImage().isEmpty()) {
                String firstImage = extractFirstImageFromSummernote(updatePost.getContent());
                existingPost.setThumbnailImage(firstImage);
            } else {
                existingPost.setThumbnailImage(updatePost.getThumbnailImage());
            }

            // 데이터베이스 업데이트
            int result = postDao.updatePost(existingPost);
            if (result <= 0) {
                throw new RuntimeException("게시글 수정에 실패했습니다.");
            }

            log.info("게시글 수정 완료 - postId: {}, 제목: {}", postId, existingPost.getTitle());
            return existingPost;

        } catch (Exception e) {
            log.error("게시글 수정 중 오류 발생 - postId: {}", postId, e);
            throw new RuntimeException("게시글 수정에 실패했습니다: " + e.getMessage());
        }
    }

    // 메인페이지용 최신글 조회 (5개)
    @Transactional(readOnly = true)
    public List<Post> getLatestPostsForMain() {
        try {
            log.info("메인페이지용 최신글 5개 조회");

            List<Post> posts = postDao.findLatestPostsForMain();

            // 게시글 데이터 후처리
            posts.forEach(this::processPostData);

            log.info("메인페이지용 최신글 조회 완료 - 조회 건수: {}", posts.size());
            return posts;

        } catch (Exception e) {
            log.error("메인페이지용 최신글 조회 중 오류 발생", e);
            return new ArrayList<>();
        }
    }
    /**
     * 메인페이지용 인기글 조회 (5개)
     * 인기도 = 조회수 + 좋아요수 + 댓글수
     */
    @Transactional(readOnly = true)
    public List<Post> getPopularPostsForMain() {
        try {
            log.info("메인페이지용 인기글 5개 조회");

            List<Post> posts = postDao.findPopularPostsForMain();

            // 게시글 데이터 후처리
            posts.forEach(this::processPostData);

            log.info("메인페이지용 인기글 조회 완료 - 조회 건수: {}", posts.size());
            return posts;

        } catch (Exception e) {
            log.error("메인페이지용 인기글 조회 중 오류 발생", e);
            return new ArrayList<>();
        }
    }

    // 메인페이지용 인기 후기 조회 (9개)
    @Transactional(readOnly = true)
    public List<Post> getPopularReviewsForMain() {
        try {
            log.info("메인페이지용 인기 후기 9개 조회");

            List<Post> posts = postDao.findPopularReviewsForMain();

            // 게시글 데이터 후처리
            posts.forEach(this::processPostData);

            log.info("메인페이지용 인기 후기 조회 완료 - 조회 건수: {}", posts.size());
            return posts;

        } catch (Exception e) {
            log.error("메인페이지용 인기 후기 조회 중 오류 발생", e);
            return new ArrayList<>();
        }
    }

    /**
     * 게시글의 좋아요 수 업데이트
     */
    @Transactional
    public void updatePostLikeCount(Integer postId) {
        try {
            postDao.updatePostLikeCount(postId);
            log.debug("게시글 좋아요 수 업데이트 완료 - postId: {}", postId);
        } catch (Exception e) {
            log.error("게시글 좋아요 수 업데이트 실패 - postId: {}", postId, e);
            throw new RuntimeException("좋아요 수 업데이트에 실패했습니다.");
        }
    }

    /**
     * 게시글의 댓글 수 업데이트
     */
    @Transactional
    public void updatePostCommentCount(Integer postId) {
        try {
            postDao.updatePostCommentCount(postId);
            log.debug("게시글 댓글 수 업데이트 완료 - postId: {}", postId);
        } catch (Exception e) {
            log.error("게시글 댓글 수 업데이트 실패 - postId: {}", postId, e);
            throw new RuntimeException("댓글 수 업데이트에 실패했습니다.");
        }
    }

    /**
     * 게시글의 모든 카운트 업데이트 (좋아요 수 + 댓글 수)
     */
    @Transactional
    public void updatePostCounts(Integer postId) {
        try {
            postDao.updatePostCounts(postId);
            log.debug("게시글 카운트 업데이트 완료 - postId: {}", postId);
        } catch (Exception e) {
            log.error("게시글 카운트 업데이트 실패 - postId: {}", postId, e);
            throw new RuntimeException("게시글 카운트 업데이트에 실패했습니다.");
        }
    }
    /**
     * 인기 게시글 페이징 조회 (관리자용)
     */
    @Transactional(readOnly = true)
    public PostListResponse getPopularPostsWithPaging(PostSearchCondition condition, int period) {
        log.info("인기 게시글 페이징 조회 - 조건: {}, 기간: {}일", condition, period);

        try {
            // 기간 설정
            condition.setPeriodDays(period);

            // 기본값 설정
            condition.setDefaults();

            // 오프셋 계산
            int offset = (condition.getPage() - 1) * condition.getSize();
            condition.setOffset(offset);

            // 인기 게시글 목록 조회
            List<Post> posts = postDao.findPopularPostsWithPaging(condition);

            // 게시글 데이터 후처리
            posts.forEach(this::processPostData);

            // 전체 개수 조회
            int totalElements = postDao.countPopularPosts(condition);

            // 페이징 정보 계산
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

            log.info("인기 게시글 페이징 조회 완료 - 총 {}개, 현재 페이지: {}/{}",
                    totalElements, condition.getPage(), totalPages);

            return response;

        } catch (Exception e) {
            log.error("인기 게시글 페이징 조회 중 오류 발생", e);
            throw new RuntimeException("인기 게시글 조회 중 오류가 발생했습니다.", e);
        }
    }

    private Long convertCountryNameToId(String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) {
            return null;
        }

        // 기존 하드코딩된 매핑을 DB 조회로 변경
        try {
            var country = countryService.getCountryByName(countryName);
            return country != null ? country.getCountryId() : null;
        } catch (Exception e) {
            log.warn("국가명 변환 중 오류 발생: {}", countryName, e);
            return null;
        }
    }
}