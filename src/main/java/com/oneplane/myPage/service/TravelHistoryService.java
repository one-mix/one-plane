package com.oneplane.myPage.service;

import com.oneplane.myPage.dao.TravelHistoryDao;
import com.oneplane.myPage.dto.TravelHistoryDto;
import com.oneplane.myPage.model.TravelHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class TravelHistoryService {

    // application.yml의 file.upload.path와 동일한 키
    @Value("${file.upload.path}")
    private String uploadDir;

    @Autowired
    private TravelHistoryDao travelHistoryDao;

    public boolean save(TravelHistoryDto dto, Long userId) {
        try {
            // 1. MultipartFile이 있으면 물리 경로에 저장하고 DTO에 파일명 세팅
            if (dto.getTravelImg() != null && !dto.getTravelImg().isEmpty()) {
                String original = dto.getTravelImg().getOriginalFilename();
                String ext = original.substring(original.lastIndexOf('.'));
                String filename = UUID.randomUUID().toString() + ext;

                // 업로드 디렉터리 자동 생성
                Path uploadPath = Paths.get(uploadDir);
                if (Files.notExists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path target = uploadPath.resolve(filename);
                dto.getTravelImg().transferTo(target);

                dto.setImagePath(filename);
            }

            // 2. 엔티티 변환 시 imagePath도 복사
            TravelHistory entity = convertToEntity(dto, userId);

            // 3. DB 저장
            return travelHistoryDao.insert(entity) > 0;
        } catch (Exception e) {
            System.err.println("여행 이력 저장 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<TravelHistory> getUserTravelHistory(Long userId) {
        return travelHistoryDao.selectByUserId(userId);
    }

    public TravelHistory getTravelHistoryById(Long id) {
        return travelHistoryDao.selectById(id);
    }

    public boolean deleteTravel(Long travelId, Long userId) {
        TravelHistory travel = travelHistoryDao.selectById(travelId);
        if (travel != null && travel.getUserId().equals(userId)) {
            return travelHistoryDao.deleteById(travelId) > 0;
        }
        return false;
    }

    public boolean updateTravel(Long travelId, TravelHistoryDto dto, Long userId) {
        TravelHistory existing = travelHistoryDao.selectById(travelId);
        if (existing == null || !existing.getUserId().equals(userId)) {
            return false;
        }

        try {
            // 1) 새 파일이 있으면 물리적으로 저장하고 DTO에 새 파일명 설정
            if (dto.getTravelImg() != null && !dto.getTravelImg().isEmpty()) {
                String original = dto.getTravelImg().getOriginalFilename();
                String ext = original.substring(original.lastIndexOf('.'));
                String filename = UUID.randomUUID().toString() + ext;

                // 업로드 디렉터리 자동 생성
                Path uploadPath = Paths.get(uploadDir);
                if (Files.notExists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path target = uploadPath.resolve(filename);
                dto.getTravelImg().transferTo(target);
                dto.setImagePath(filename);  // 새 파일명으로 DTO 업데이트
            }
            // else: 새 파일이 없으면 dto.imagePath는 hidden 필드에서 온 기존 경로 유지

            // 2) DTO → 엔티티 변환
            TravelHistory updated = convertToEntity(dto, userId);
            updated.setTravelHistoryId(travelId);

            // 3) 새 파일이 없을 때 기존 BLOB 데이터 유지
            if (dto.getTravelImg() == null || dto.getTravelImg().isEmpty()) {
                updated.setTravelImg(existing.getTravelImg());
            }

            System.out.println("=== 최종 업데이트 정보 ===");
            System.out.println("업데이트될 imagePath: " + updated.getImagePath());
            System.out.println("BLOB 데이터 있음: " + (updated.getTravelImg() != null));

            // 4) DB 업데이트
            return travelHistoryDao.update(updated) > 0;

        } catch (Exception e) {
            System.err.println("이미지 업로드 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }





    private TravelHistory convertToEntity(TravelHistoryDto dto, Long userId) {
        TravelHistory entity = new TravelHistory();
        entity.setUserId(userId);
        entity.setCountryId(dto.getCountryId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());

        LocalDate ld = LocalDate.parse(dto.getTravelDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Date sqlDate = Date.valueOf(ld);
        entity.setTravelAt(sqlDate);

        entity.setCity(dto.getCity());
        entity.setTravelPurpose(dto.getTravelPurpose());
        entity.setCompanion(dto.getCompanion());
        entity.setRating(dto.getRating());

        // 파일명(path) 필드 복사
        entity.setImagePath(dto.getImagePath());

        // BLOB 저장이 필요하면 MultipartFile에서 bytes 추출
        if (dto.getTravelImg() != null && !dto.getTravelImg().isEmpty()) {
            try {
                entity.setTravelImg(dto.getTravelImg().getBytes());
            } catch (Exception e) {
                throw new RuntimeException("이미지 변환 실패", e);
            }
        }
        return entity;
    }

    /** 등록한 여행 사진 개수 반환 */
    public int getUploadedPhotoCount(Long userId) {
        return travelHistoryDao.getTravelPhotoCount(userId);
    }
}
