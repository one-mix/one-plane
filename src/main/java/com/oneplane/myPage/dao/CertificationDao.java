package com.oneplane.myPage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CertificationDao {

    @Autowired
    private JdbcTemplate jdbc;

    public int insertCertification(Long userId, Long countryId, String certificationImgPath, String certificationDate) {
        // 디버깅 로그 추가
        System.out.println("=== DAO 삽입 디버그 ===");
        System.out.println("userId: " + userId);
        System.out.println("countryId: " + countryId);
        System.out.println("certificationImgPath: " + certificationImgPath);
        System.out.println("certificationDate: " + certificationDate);

        // 날짜 유효성 검사
        if (certificationDate == null || certificationDate.trim().isEmpty()) {
            System.err.println("DAO: 인증 날짜가 null 또는 빈 문자열입니다.");
            throw new IllegalArgumentException("인증 날짜는 필수입니다.");
        }

        String sql = """
            INSERT INTO travel_certification (
                certification_id, user_id, country_id, certification_img, certification_at, created_at
            ) VALUES (
                travel_certification_seq.NEXTVAL, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), SYSDATE
            )
            """;

        return jdbc.update(sql, userId, countryId, certificationImgPath, certificationDate);
    }

    public List<Map<String, Object>> getUserCertifiedCountries(Long userId) {
        String sql = """
            SELECT tc.*, c.country_name, c.img, c.distance
            FROM travel_certification tc
            JOIN country c ON tc.country_id = c.country_id
            WHERE tc.user_id = ?
            ORDER BY tc.certification_at DESC
            """;

        return jdbc.queryForList(sql, userId);
    }

    /** 다녀온 국가 수 */
    public int getCertificationCount(Long userId) {
        String sql = """
            SELECT COUNT(*) 
              FROM travel_certification 
             WHERE user_id = ?
        """;
        try {
            return jdbc.queryForObject(sql, Integer.class, userId);
        } catch (Exception e) {
            return 0;
        }
    }


    /** 총 이동 거리 합계 */
    public int getTotalCertificationDistance(Long userId) {
        String sql = """
            SELECT NVL(SUM(c.distance),0)
              FROM travel_certification tc
              JOIN country c ON tc.country_id = c.country_id
             WHERE tc.user_id = ?
        """;
        try {
            Number result = jdbc.queryForObject(sql, Number.class, userId);
            return result != null ? result.intValue() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getTotalPhotosCount(Long userId) {
        String sql = """
            SELECT
            (SELECT COUNT(*) FROM travel_history WHERE user_id = ? AND travel_img IS NOT NULL AND deleted_at IS NULL) +
            (SELECT COUNT(*) FROM travel_certification WHERE user_id = ? AND certification_img IS NOT NULL) AS total_photos
            FROM dual
            """;

        try {
            return jdbc.queryForObject(sql, Integer.class, userId, userId);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 사용자 인증 내역에서
     * - 인증 날짜(certification_at)
     * - 국가 국기 이미지 URL(img)
     * - 해당 국가까지의 거리(distance)
     * 를 조회하여 반환
     */
    public List<Map<String, Object>> fetchCertifications(Long userId) {
        String sql = """
            SELECT
                TO_CHAR(tc.certification_at, 'YYYY.MM.DD') AS certificationAt,
                c.img                             AS img,
                c.distance                        AS distance
            FROM travel_certification tc
            JOIN country c ON tc.country_id = c.country_id
            WHERE tc.user_id = ?
            ORDER BY tc.certification_at ASC
            """;
        return jdbc.queryForList(sql, userId);
    }
}
