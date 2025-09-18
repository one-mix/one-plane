package com.oneplane.myPage.service;

import com.oneplane.myPage.dao.UserDao;
import com.oneplane.myPage.dto.UserProfileDto;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 사용자 프로필 관리 서비스
 * 프로필 이미지 저장 및 사용자 정보 조회/수정 로직 제공
 *
 * 작성자: 허겸
 */
@Service
public class UserProfileService {

    @Value("${file.upload.path}")
    private String uploadDir;      // 외부 업로드 기본 경로

    @Autowired
    private UserDao userDao;      // 사용자 DAO

    /**
     * 프로필 이미지 업데이트
     * 서버에 파일 저장 후 DB에 경로 저장
     *
     * @param userId    사용자 ID
     * @param imageFile 업로드된 이미지 파일
     * @return 저장 성공 여부
     */
    public boolean updateProfileImage(Long userId, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return false;               // 파일 없으면 실패
        }
        try {
            // 파일명 정리 및 확장자 추출
            String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String ext = FilenameUtils.getExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + "." + ext;

            // 업로드 경로: {uploadDir}/profile
            Path uploadPath = Paths.get(uploadDir, "profile");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 복사
            Path targetPath = uploadPath.resolve(newFilename);
            try (InputStream in = imageFile.getInputStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // DB 저장 경로 생성
            String dbPath = "/uploads/profile/" + newFilename;
            return userDao.updateProfileImage(userId, dbPath) > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;               // 저장 실패
        }
    }

    /**
     * 사용자 전체 프로필 조회
     *
     * @param userId 사용자 ID
     * @return UserProfileDto 사용자 프로필 데이터
     */
    public UserProfileDto getUserInfo(Long userId) {
        return userDao.getUserFullProfile(userId);
    }

    /**
     * 사용자 프로필 정보 업데이트
     * 닉네임, 소개 등 프로필 전반 수정
     *
     * @param userId 사용자 ID
     * @param dto    수정할 프로필 정보 DTO
     * @return 성공 여부
     */
    public boolean updateProfile(Long userId, UserProfileDto dto) {
        try {
            return userDao.updateUserProfile(userId, dto) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 사용자 닉네임만 업데이트
     *
     * @param userId   사용자 ID
     * @param nickname 새로운 닉네임
     * @return 성공 여부
     */
    public boolean updateNickname(Long userId, String nickname) {
        try {
            return userDao.updateNickname(userId, nickname) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}