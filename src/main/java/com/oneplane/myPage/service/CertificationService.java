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

@Service
public class CertificationService {

    @Autowired
    private CertificationDao certificationDao;

    @Autowired
    private MypageCountryDao mypageCountryDao;

    @Autowired
    private UserDao userDao;

    public boolean registerCountryCertification(CertificationDto dto, Long userId) {
        try {
            Long countryId = mypageCountryDao.findCountryIdByName(dto.getCountryName());
            if (countryId == null) {
                throw new IllegalArgumentException("등록되지 않은 국가명: " + dto.getCountryName());
            }

            String imgPath = null;
            if (dto.getCertificationImg() != null && !dto.getCertificationImg().isEmpty()) {
                imgPath = saveCertificationImage(userId, dto.getCertificationImg());
                if (imgPath == null) {
                    return false;
                }
            }

            int inserted = certificationDao.insertCertification(userId, countryId, imgPath, dto.getCertificationDate());
            return inserted > 0;
        } catch (Exception e) {
            System.err.println("국가 인증 등록 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<?> getUserCertifications(Long userId) {
        return certificationDao.getUserCertifiedCountries(userId);
    }

    private String saveCertificationImage(Long userId, MultipartFile imageFile) {
        try {
            String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String ext = FilenameUtils.getExtension(originalFilename);
            String newFilename = "cert_" + userId + "_" + UUID.randomUUID().toString() + "." + ext;

            Path uploadPath = Paths.get("src/main/resources/static/uploads/certification");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path targetPath = uploadPath.resolve(newFilename);
            try (InputStream in = imageFile.getInputStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/uploads/certification/" + newFilename;
        } catch (IOException e) {
            System.err.println("인증 이미지 저장 실패: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public int getVisitedCountryCount(Long userId) {
        return certificationDao.getCertificationCount(userId);
    }

    public int getVisitedTotalDistance(Long userId) {
        return certificationDao.getTotalCertificationDistance(userId);
    }

    // ★ 수정된 부분 ★
    public List<CertificationTimelineDto> getTimelineData(Long userId) {
        List<Map<String, Object>> rows = certificationDao.fetchCertifications(userId);
        int cumulative = 0;
        List<CertificationTimelineDto> list = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            CertificationTimelineDto dto = new CertificationTimelineDto();
            // Spring JDBC는 컬럼명을 대문자로 변환하므로 키를 대문자로 사용
            dto.setCertificationDate((String) row.get("CERTIFICATIONAT"));  // 수정!
            dto.setCountryFlagUrl((String) row.get("IMG"));                 // 수정!
            Number distNum = (Number) row.get("DISTANCE");
            int dist = distNum != null ? distNum.intValue() : 0;
            dto.setDistance(dist);
            cumulative += dist;
            dto.setCumulativeDistance(cumulative);
            list.add(dto);
        }
        return list;
    }

    public int getTotalCertificationDistance(Long userId) {
        return certificationDao.getTotalCertificationDistance(userId);
    }

    public int getTotalPhotosCount(Long userId) {
        return certificationDao.getTotalPhotosCount(userId);
    }
}
