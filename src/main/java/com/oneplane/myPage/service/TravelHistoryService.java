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

/**
 * 여행 기록 관리 서비스
 * 여행 이력의 생성, 조회, 수정, 삭제 및 DTO-Entity 변환 로직 처리
 *
 * @author 허겸
 */
@Service
@Transactional
public class TravelHistoryService {

    @Autowired
    private TravelHistoryDao travelHistoryDao;  // DAO for travel history operations

    @Autowired
    private JdbcTemplate jdbc;                 // JDBC template for custom SQL queries

    /**
     * 새로운 여행 기록 저장
     * DTO를 Entity로 변환 후 DAO를 통해 DB에 삽입
     *
     * @param dto    사용자 입력 데이터
     * @param userId 세션 사용자 ID
     * @return 삽입 성공 여부
     */
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

    /**
     * country_id를 통해 국가명을 조회
     *
     * @param countryId 국가 ID
     * @return 국가명 또는 null
     */
    public String findCountryCodeById(Long countryId) {
        String sql = "SELECT country_name FROM country WHERE country_id = ?";
        try {
            return jdbc.queryForObject(sql, String.class, countryId);
        } catch (EmptyResultDataAccessException e) {
            return null;  // 해당 국가가 없을 경우
        }
    }

    /**
     * 사용자의 모든 여행 기록 조회
     * 읽기 전용 트랜잭션 적용
     *
     * @param userId 사용자 ID
     * @return 여행 기록 리스트
     */
    @Transactional(readOnly = true)
    public List<TravelHistory> getUserTravelHistory(Long userId) {
        return travelHistoryDao.selectByUserId(userId);
    }

    /**
     * ID로 단일 여행 기록 조회
     * 읽기 전용 트랜잭션 적용
     *
     * @param id 여행 기록 ID
     * @return TravelHistory 엔티티
     */
    @Transactional(readOnly = true)
    public TravelHistory getTravelHistoryById(Long id) {
        return travelHistoryDao.selectById(id);
    }

    /**
     * 여행 기록 삭제
     * 소유자 UID 검증 후 삭제 수행
     *
     * @param travelId 여행 기록 ID
     * @param userId   요청 사용자 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteTravel(Long travelId, Long userId) {
        try {
            TravelHistory travel = travelHistoryDao.selectById(travelId);
            if (travel != null && travel.getUserId().equals(userId)) {
                int result = travelHistoryDao.deleteById(travelId);
                return result > 0;
            }
            return false;  // 권한 없거나 기록 없음
        } catch (Exception e) {
            System.err.println("여행 이력 삭제 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("여행 이력 삭제 실패", e);
        }
    }

    /**
     * 여행 기록 수정
     * 기존 데이터 로드 후 권한 검증, DTO 변환 및 조건에 따른 이미지 처리
     *
     * @param travelId 수정할 여행 기록 ID
     * @param dto      사용자 입력 DTO
     * @param userId   요청 사용자 ID
     * @return 수정 성공 여부
     */
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
            // 이미지 교체 여부 확인
            if (dto.getTravelImg() == null || dto.getTravelImg().isEmpty()) {
                System.out.println("기존 이미지 유지");
                updated.setTravelImg(existing.getTravelImg());
            } else {
                System.out.println("새 이미지로 교체");
                // 변환 로직 내에 처리됨
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

    /**
     * DTO를 Entity로 변환
     * countryCode→countryId 매핑, 날짜 및 이미지 바이트 변환 처리
     *
     * @param dto    입력 DTO
     * @param userId 사용자 ID
     * @return TravelHistory 엔티티
     */
    private TravelHistory convertToEntity(TravelHistoryDto dto, Long userId) {
        System.out.println("=== DTO to Entity 변환 시작 ===");
        TravelHistory entity = new TravelHistory();
        entity.setUserId(userId);
        // 국가명으로 ID 조회
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
        // 이미지 바이트 변환
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

    /**
     * 사용자가 업로드한 사진 개수 조회
     * 읽기 전용 트랜잭션 적용
     *
     * @param userId 사용자 ID
     * @return 사진 개수
     */
    @Transactional(readOnly = true)
    public int getUploadedPhotoCount(Long userId) {
        return travelHistoryDao.getTravelPhotoCount(userId);
    }

    /**
     * countryId로 국가명 조회
     * 읽기 전용 트랜잭션 적용
     *
     * @param countryId 국가 ID
     * @return 국가명
     */
    @Transactional(readOnly = true)
    public String findCountryNameById(Long countryId) {
        return travelHistoryDao.findCountryNameById(countryId);
    }
}