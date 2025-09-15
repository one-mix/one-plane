package com.oneplane.myPage.dao;

import com.oneplane.myPage.dto.UserProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbc;

    // 전체 사용자 프로필 정보 조회
    public UserProfileDto getUserFullProfile(Long userId) {
        String sql = """
            SELECT nickname, name, age, gender, profile_img, grade
            FROM users 
            WHERE user_id = ?
        """;

        RowMapper<UserProfileDto> mapper = (rs, rowNum) -> {
            UserProfileDto dto = new UserProfileDto();
            dto.setNickname(rs.getString("nickname"));
            dto.setName(rs.getString("name"));
            dto.setAge(rs.getInt("age"));
            dto.setGender(rs.getString("gender"));

            String profileImg = rs.getString("profile_img");
            if (profileImg != null && !profileImg.isEmpty()) {
                // 캐시 방지를 위한 타임스탬프 추가
                profileImg += "?t=" + System.currentTimeMillis();
            }
            dto.setProfileImagePath(profileImg);
            dto.setGrade(rs.getString("grade"));  // 추가
            return dto;
        };

        try {
            return jdbc.queryForObject(sql, mapper, userId);
        } catch (EmptyResultDataAccessException e) {
            return new UserProfileDto(); // 빈 객체 반환
        }
    }

    public int updateProfileImage(Long userId, String imagePath) {
        String sql = "UPDATE users SET profile_img = ? WHERE user_id = ?";
        return jdbc.update(sql, imagePath, userId);
    }

    public String getProfileImage(Long userId) {
        String sql = "SELECT profile_img FROM users WHERE user_id = ?";
        try {
            return jdbc.queryForObject(sql, String.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String getUserNickname(Long userId) {
        String sql = "SELECT nickname FROM users WHERE user_id = ?";
        try {
            return jdbc.queryForObject(sql, String.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 닉네임만 업데이트
    public int updateNickname(Long userId, String nickname) {
        String sql = "UPDATE users SET nickname = ? WHERE user_id = ?";
        return jdbc.update(sql, nickname, userId);
    }

    public int updateUserProfile(Long userId, UserProfileDto dto) {
        String sql = """
            UPDATE users 
            SET nickname = ?, name = ?, age = ?, gender = ? 
            WHERE user_id = ?
        """;
        return jdbc.update(sql,
                dto.getNickname(),
                dto.getName(),
                dto.getAge(),
                dto.getGender(),
                userId);
    }
}
