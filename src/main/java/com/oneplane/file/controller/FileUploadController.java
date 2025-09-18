// 작성자: 김동현
package com.oneplane.file.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/upload")
@Slf4j
public class FileUploadController {

    @Value("${file.upload.path:/uploads/}")
    private String uploadPath;

    @Value("${file.upload.url:/uploads/}")
    private String uploadUrl;

    // Summernote 에디터 이미지 업로드
    @PostMapping("/image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("image") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 파일 유효성 검사
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 파일 크기 제한 (5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                response.put("success", false);
                response.put("message", "파일 크기는 5MB를 초과할 수 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 이미지 파일만 허용
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("success", false);
                response.put("message", "이미지 파일만 업로드 가능합니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 파일 저장
            String savedFileName = saveFile(file);
            String imageUrl = uploadUrl + savedFileName;

            response.put("success", true);
            response.put("imageUrl", imageUrl);
            response.put("fileName", savedFileName);

            log.info("이미지 업로드 성공: {}", imageUrl);

        } catch (Exception e) {
            log.error("이미지 업로드 실패", e);
            response.put("success", false);
            response.put("message", "이미지 업로드 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }

    // 파일 저장 처리
    private String saveFile(MultipartFile file) throws IOException {
        // 업로드 디렉토리 생성
        createUploadDirectory();

        // 원본 파일명과 확장자 추출
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // 고유한 파일명 생성 (날짜 + UUID)
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniqueFileName = datePrefix + "_" + UUID.randomUUID().toString() + extension;

        // 파일 저장 경로
        Path uploadDir = Paths.get(uploadPath);
        Path filePath = uploadDir.resolve(uniqueFileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }


    // 업로드 디렉토리 생성
    private void createUploadDirectory() throws IOException {
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
            log.info("업로드 디렉토리 생성: {}", uploadDir.toAbsolutePath());
        }
    }
}
