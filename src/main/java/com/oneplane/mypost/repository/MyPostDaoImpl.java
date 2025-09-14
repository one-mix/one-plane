package com.oneplane.mypost.repository;

import com.oneplane.mypost.dao.MyPostDao;
import com.oneplane.post.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class MyPostDaoImpl implements MyPostDao {

    @Autowired
    private SqlSession sqlSession;

    // 기존 PostMapper의 selectPostsByAuthor 사용
    private final String namespace = "com.oneplane.post.mapper.PostMapper.";

    @Override
    public List<Post> selectPostsByAuthor(Integer userId) {
        log.debug("내 게시글 전체 조회 - userId: {}", userId);
        return sqlSession.selectList(namespace + "selectPostsByAuthor", userId);
    }

    @Override
    public List<Post> selectPostsByAuthorAndCategory(Integer userId, String category) {
        log.debug("내 게시글 카테고리별 조회 - userId: {}, category: {}", userId, category);

        // 파라미터 2개를 Map으로 전달
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("category", category);

        return sqlSession.selectList(namespace + "selectPostsByAuthorAndCategory", params);
    }




}