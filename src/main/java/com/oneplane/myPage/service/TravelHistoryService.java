package com.oneplane.myPage.service;

import com.oneplane.myPage.dao.TravelHistoryDao;
import com.oneplane.myPage.dto.TravelHistoryDto;
import com.oneplane.myPage.domain.TravelHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class TravelHistoryService {

    @Autowired
    private TravelHistoryDao travelHistoryDao;
    private JdbcTemplate jdbc;



    public boolean save(TravelHistoryDto dto, Long userId) {
        System.out.println("=== Service 저장 시작 ===");

        try {
            TravelHistory entity = convertToEntity(dto, userId);
            System.out.println("Entity 변환 완료");

            int result = travelHistoryDao.insert(entity);
            System.out.println("DAO insert 결과: " + result);

            return result > 0;
        } catch (Exception e) {
            System.err.println("여행 이력 저장 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("여행 이력 저장 실패", e);
        }
    }

    /** country_id로 countryName 조회 */
    public String findCountryCodeById(Long countryId) {
        // country 테이블에 실제 컬럼명이 country_name이라면 이처럼 작성
        String sql = "SELECT country_name FROM country WHERE country_id = ?";
        try {
            return jdbc.queryForObject(sql, String.class, countryId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<TravelHistory> getUserTravelHistory(Long userId) {
        return travelHistoryDao.selectByUserId(userId);
    }

    @Transactional(readOnly = true)
    public TravelHistory getTravelHistoryById(Long id) {
        return travelHistoryDao.selectById(id);
    }

    public boolean deleteTravel(Long travelId, Long userId) {
        try {
            TravelHistory travel = travelHistoryDao.selectById(travelId);
            if (travel != null && travel.getUserId().equals(userId)) {
                int result = travelHistoryDao.deleteById(travelId);
                return result > 0;
            }
            return false;
        } catch (Exception e) {
            System.err.println("여행 이력 삭제 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("여행 이력 삭제 실패", e);
        }
    }

    public boolean updateTravel(Long travelId, TravelHistoryDto dto, Long userId) {
        System.out.println("=== Service 수정 시작 ===");

        try {
            TravelHistory existing = travelHistoryDao.selectById(travelId);
            if (existing == null || !existing.getUserId().equals(userId)) {
                System.out.println("수정 권한 없음");
                return false;
            }

            TravelHistory updated = convertToEntity(dto, userId);
            updated.setTravelHistoryId(travelId);

            // 새로운 이미지가 없다면 기존 이미지 유지
            if (dto.getTravelImg() == null || dto.getTravelImg().isEmpty()) {
                System.out.println("기존 이미지 유지");
                updated.setTravelImg(existing.getTravelImg());
            } else {
                System.out.println("새 이미지로 교체");
            }

            int result = travelHistoryDao.update(updated);
            System.out.println("DAO update 결과: " + result);

            return result > 0;
        } catch (Exception e) {
            System.err.println("여행 이력 수정 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("여행 이력 수정 실패", e);
        }
    }

    private TravelHistory convertToEntity(TravelHistoryDto dto, Long userId) {
        System.out.println("=== DTO to Entity 변환 시작 ===");

        TravelHistory entity = new TravelHistory();
        entity.setUserId(userId);

        // DTO의 countryCode(String) → DAO로 country_id(Long) 변환
        Long countryId = travelHistoryDao.findCountryIdByName(dto.getCountryCode());
        if (countryId == null) {
            throw new RuntimeException("알 수 없는 국가명: " + dto.getCountryCode());
        }
        entity.setCountryId(countryId);

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());

        // 날짜 변환
        try {
            LocalDate ld = LocalDate.parse(dto.getTravelDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Date sqlDate = Date.valueOf(ld);
            entity.setTravelAt(sqlDate);
            System.out.println("날짜 변환 완료: " + sqlDate);
        } catch (Exception e) {
            System.err.println("날짜 변환 실패: " + dto.getTravelDate());
            throw new RuntimeException("날짜 형식이 잘못되었습니다: " + dto.getTravelDate(), e);
        }

        entity.setCity(dto.getCity());
        entity.setTravelPurpose(dto.getTravelPurpose());
        entity.setCompanion(dto.getCompanion());
        entity.setRating(dto.getRating());

        // 이미지 처리
        if (dto.getTravelImg() != null && !dto.getTravelImg().isEmpty()) {
            try {
                byte[] imageBytes = dto.getTravelImg().getBytes();
                entity.setTravelImg(imageBytes);
                System.out.println("이미지 변환 완료: " + imageBytes.length + " bytes");
            } catch (Exception e) {
                System.err.println("이미지 변환 실패: " + e.getMessage());
                throw new RuntimeException("이미지 변환 실패", e);
            }
        } else {
            System.out.println("이미지 없음");
        }

        return entity;
    }

    /** 등록한 여행 사진 개수 반환 */
    @Transactional(readOnly = true)
    public int getUploadedPhotoCount(Long userId) {
        return travelHistoryDao.getTravelPhotoCount(userId);
    }

    @Transactional(readOnly = true)
    public String findCountryNameById(Long countryId) {
        return travelHistoryDao.findCountryNameById(countryId);
    }
}
