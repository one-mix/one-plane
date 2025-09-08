package com.oneplane.post.dao;

import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostSearchCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostDao {
    // 게시글 전체 조회
    List<Post> findAllPosts();

    // 페이징된 게시글 목록 조회
    List<Post> findPostsWithPaging(PostSearchCondition condition);

    // 검색 조건에 따른 총 게시글 수 조회
    int countPosts(PostSearchCondition condition);

    // 카테고리별 게시글 목록 조회
    List<Post> findPostsByCategory(@Param("category") String category,
                                   @Param("offset") int offset,
                                   @Param("size") int size);

    // 카테고리별 게시글 수 조회
    int countPostsByCategory(@Param("category") String category);

    int insertPost(Post post);

    // 게시글 ID로 조회
    Post findPostById(@Param("postId") Integer postId);

    // 조회수 증가
    int increaseViewCount(@Param("postId") Integer postId);

    // 게시글 삭제
    int deletePost(@Param("postId") Integer postId);

    // 게시글 수정
    int updatePost(Post post);
}
