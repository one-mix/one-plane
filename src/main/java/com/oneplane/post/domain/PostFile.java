// 작성자: 김동현
package com.oneplane.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFile {
    private Integer fileId;
    private Integer postId;
    private Integer userId;
    private String originalFilename;     // 원본 파일명
    private String storedFilename;       // 저장된 파일명 (UUID 등)
    private String filePath;             // 파일 저장 경로
    private Long fileSize;               // 파일 크기
    private String fileType;             // jpeg, png
    private Date createdAt;

    /**
     * 이미지 파일 여부 확인
     * 작성자 : 김동현
     */
    public boolean isImage() {
        return fileType != null && fileType.startsWith("image/");
    }

    /**
     * 파일 크기를 읽기 쉬운 형태로 포맷팅
     * 작성자 : 김동현
     */
    public String getFormattedFileSize() {
        if (fileSize == null) return "0 B";

        String[] units = {"B", "KB", "MB", "GB"};
        double size = fileSize.doubleValue();
        int unitIndex = 0;

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.1f %s", size, units[unitIndex]);
    }

    /**
     * 파일 확장자 반환
     * 작성자 : 김동현
     */
    public String getFileExtension() {
        if (originalFilename == null) return "";
        int lastDot = originalFilename.lastIndexOf('.');
        return lastDot > 0 ? originalFilename.substring(lastDot + 1).toLowerCase() : "";
    }

    /**
     * 임시 파일 여부 확인
     * 작성자 : 김동현
     */
    public boolean isTemporary() {
        return postId == null;
    }
}