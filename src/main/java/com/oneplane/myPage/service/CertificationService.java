package com.oneplane.myPage.service;

import com.oneplane.myPage.dao.CertificationDao;
import com.oneplane.myPage.dao.MypageCountryDao;
import com.oneplane.myPage.dao.UserDao;
import com.oneplane.myPage.dto.CertificationDto;
import com.oneplane.myPage.dto.CertificationTimelineDto;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 국가 인증 관련 비즈니스 로직을 처리하는 서비스
 * 사용자의 국가 인증 등록, 조회, 타임라인 데이터 생성, 통계 정보 제공 기능 포함
 *
 * 작성자: 허겸
 */
@Service
public class CertificationService {

    @Autowired
    private CertificationDao certificationDao;

    @Autowired
    private MypageCountryDao mypageCountryDao;

    @Autowired
    private UserDao userDao;

    /**
     * 사용자 국가 인증 등록 처리
     * 국가명으로 ID 조회, 이미지 저장 후 DB에 인증 정보 삽입
     *
     * @param dto 인증 정보 DTO (국가명, 이미지, 날짜 등)
     * @param userId 사용자 ID
     * @return 성공 여부
     */
    public boolean registerCountryCertification(CertificationDto dto, Long userId) {
        try {
            // 국가명으로 countryId 조회
            Long countryId = mypageCountryDao.findCountryIdByName(dto.getCountryName());
            if (countryId == null) {
                throw new IllegalArgumentException("등록되지 않은 국가명: " + dto.getCountryName());
            }

            // 인증 이미지가 있으면 저장
            String imgPath = null;
            if (dto.getCertificationImg() != null && !dto.getCertificationImg().isEmpty()) {
                imgPath = saveCertificationImage(userId, dto.getCertificationImg());
                if (imgPath == null) {
                    return false; // 이미지 저장 실패
                }
            }

            // DB에 인증 정보 삽입
            int inserted = certificationDao.insertCertification(
                    userId,
                    countryId,
                    imgPath,
                    dto.getCertificationDate()
            );
            return inserted > 0;
        } catch (Exception e) {
            System.err.println("국가 인증 등록 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 사용자의 국가 인증 목록 조회
     *
     * @param userId 사용자 ID
     * @return 국가 인증 DTO 리스트
     */
    public List<Map<String, Object>> getUserCertifications(Long userId) {
        return certificationDao.getUserCertifiedCountries(userId);
    }

    /**
     * 인증 이미지 파일을 서버에 저장하고 URL 경로를 반환
     * 파일명 중복 방지를 위해 UUID 사용
     *
     * @param userId 사용자 ID
     * @param imageFile 업로드된 이미지 파일
     * @return 저장된 이미지 접근 경로 또는 null
     */
    private String saveCertificationImage(Long userId, MultipartFile imageFile) {
        try {
            // 원본 파일명 정리 및 확장자 추출
            String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String ext = FilenameUtils.getExtension(originalFilename);
            String newFilename = "cert_" + userId + "_" + UUID.randomUUID().toString() + "." + ext;

            // 업로드 디렉토리 생성
            Path uploadPath = Paths.get("src/main/resources/static/uploads/certification");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 복사
            Path targetPath = uploadPath.resolve(newFilename);
            try (InputStream in = imageFile.getInputStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 웹에서 접근 가능한 경로 반환
            return "/uploads/certification/" + newFilename;
        } catch (IOException e) {
            System.err.println("인증 이미지 저장 실패: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 방문한 국가 수 조회
     *
     * @param userId 사용자 ID
     * @return 국가 수
     */
    public int getVisitedCountryCount(Long userId) {
        return certificationDao.getCertificationCount(userId);
    }

    /**
     * 총 인증 거리 합계 조회
     *
     * @param userId 사용자 ID
     * @return 총 인증 거리
     */
    public int getVisitedTotalDistance(Long userId) {
        return certificationDao.getTotalCertificationDistance(userId);
    }

    /**
     * 타임라인 표시용 DTO 리스트 생성
     * DB에서 인증 기록 조회 후 누적 거리 계산하여 서비스 로직 처리
     *
     * @param userId 사용자 ID
     * @return 인증 타임라인 DTO 리스트
     */
    public List<CertificationTimelineDto> getTimelineData(Long userId) {
        List<Map<String, Object>> rows = certificationDao.fetchCertifications(userId);
        int cumulative = 0;
        List<CertificationTimelineDto> list = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            CertificationTimelineDto dto = new CertificationTimelineDto();
            // JDBC 결과 키는 대문자
            dto.setCertificationDate((String) row.get("CERTIFICATIONAT"));
            dto.setCountryFlagUrl((String) row.get("IMG"));
            Number distNum = (Number) row.get("DISTANCE");
            int dist = distNum != null ? distNum.intValue() : 0;
            dto.setDistance(dist);
            cumulative += dist;                 // 누적 거리 계산
            dto.setCumulativeDistance(cumulative);
            list.add(dto);
        }
        return list;
    }

    /**
     * 총 인증 거리 합계 조회 (타임라인 스케일링 등 UI 처리용)
     *
     * @param userId 사용자 ID
     * @return 총 인증 거리
     */
    public int getTotalCertificationDistance(Long userId) {
        return certificationDao.getTotalCertificationDistance(userId);
    }

    /**
     * 인증 사진 총 개수 조회
     *
     * @param userId 사용자 ID
     * @return 사진 개수
     */
    public int getTotalPhotosCount(Long userId) {
        return certificationDao.getTotalPhotosCount(userId);
    }
}