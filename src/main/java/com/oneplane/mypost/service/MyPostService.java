package com.oneplane.mypost.service;

import com.oneplane.mypost.dao.MyPostDao;
import com.oneplane.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MyPostService {

    private final MyPostDao myPostDao;

    /**
     * 내 게시글 전체 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Post> getMyPosts(Integer userId) {
        log.info("내 게시글 전체 목록 조회 요청 - userId: {}", userId);

        try {
            // 기존 selectPostsByAuthor 메서드 사용
            List<Post> posts = myPostDao.selectPostsByAuthor(userId);
            log.info("내 게시글 전체 조회 완료 - 조회 건수: {}", posts.size());
            return posts;

        } catch (Exception e) {
            log.error("내 게시글 목록을 불러오는데 실패했습니다.", e);
            return new ArrayList<>();
        }
    }

    public List<Post> getMyPostsByCategory(Integer userId, String category) {
        log.info("내 게시글 전체 목록 카테고리 요청 -userId: {}, category: {}", userId, category);

        try{
            List<Post> posts = myPostDao.selectPostsByAuthorAndCategory(userId, category);
            log.info("내 게시글 전체 조회 카테고리 조회 완료 - 조회 건수: {} ", posts.size());
            return posts;
        } catch (Exception e){
            log.error("내 게시글 목록과 카테고리를 불러오는데 실패하였습니다.", e);
            return new ArrayList<>();
        }
    }


}