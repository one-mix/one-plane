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

@Service
public class UserProfileService {

    @Value("${file.upload.path}")
    private String uploadDir;  // 외부 업로드 루트 경로 (./uploads/)

    @Autowired
    private UserDao userDao;

    public boolean updateProfileImage(Long userId, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return false;
        }
        try {
            String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String ext = FilenameUtils.getExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + "." + ext;

            // file.upload.path (e.g. "./uploads/") + "profile"
            Path uploadPath = Paths.get(uploadDir, "profile");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path targetPath = uploadPath.resolve(newFilename);
            try (InputStream in = imageFile.getInputStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // DB에 저장되는 URL도 file.upload.url (/uploads/) + "profile/" + newFilename
            String dbPath = "/uploads/profile/" + newFilename;
            return userDao.updateProfileImage(userId, dbPath) > 0;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserProfileDto getUserInfo(Long userId) {
        return userDao.getUserFullProfile(userId);
    }

    public boolean updateProfile(Long userId, UserProfileDto dto) {
        try {
            return userDao.updateUserProfile(userId, dto) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 닉네임만 업데이트
    public boolean updateNickname(Long userId, String nickname) {
        try {
            return userDao.updateNickname(userId, nickname) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
