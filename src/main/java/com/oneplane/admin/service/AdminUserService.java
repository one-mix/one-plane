package com.oneplane.admin.service;

import com.oneplane.admin.dao.AdminUserDao;
import com.oneplane.admin.domain.AdminUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminUserService {

    private final AdminUserDao adminUserDao;


    // 전체 사용자 목록 조회 (페이징, 검색)
    @Transactional(readOnly = true)
    public Map<String, Object> getUserList(int page, int pageSize, String search) {
        log.debug("사용자 목록 조회 - page: {}, pageSize: {}, search: {}", page, pageSize, search);

        // 페이지 검증
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 20;

        // offset 계산
        int offset = (page - 1) * pageSize;

        // 검색 조건 정리
        String searchKeyword = (search != null && !search.trim().isEmpty()) ? search.trim() : null;

        // 사용자 목록 조회
        List<AdminUser> users = adminUserDao.findUserList(offset, pageSize, searchKeyword);

        // 전체 개수 조회
        int totalCount = adminUserDao.getTotalUserCount(searchKeyword);

        // 페이지 정보 계산
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        boolean hasNext = page < totalPages;
        boolean hasPrevious = page > 1;

        // 결과 Map 생성
        Map<String, Object> result = new HashMap<>();
        result.put("users", users);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalCount", totalCount);
        result.put("pageSize", pageSize);
        result.put("hasNext", hasNext);
        result.put("hasPrevious", hasPrevious);

        log.debug("사용자 목록 조회 완료 - 총 {}명, {}페이지/{}", totalCount, page, totalPages);

        return result;
    }
}
