package com.oneplane.myPage.dao;

import com.oneplane.myPage.model.TravelHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TravelHistoryDao implements ITravelHistoryDao {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<TravelHistory> rowMapper = new RowMapper<TravelHistory>() {
        @Override
        public TravelHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            TravelHistory th = new TravelHistory();
            th.setTravelHistoryId(rs.getLong("travel_history_id"));
            th.setUserId(rs.getLong("user_id"));
            th.setCountryId(rs.getLong("country_id"));
            th.setTitle(rs.getString("title"));
            th.setContent(rs.getString("content"));
            th.setTravelAt(rs.getDate("travel_at"));
            th.setTravelImg(rs.getBytes("travel_img"));
            th.setImagePath(rs.getString("image_path"));
            th.setCity(rs.getString("city"));
            th.setTravelPurpose(rs.getString("travel_purpose"));
            th.setCompanion(rs.getString("companion"));
            th.setRating(rs.getInt("rating"));
            return th;
        }
    };

    @Override
    public int insert(TravelHistory travel) {
        String sql = """
    INSERT INTO travel_history (
    travel_history_id, user_id, country_id, title, content, travel_at,
    travel_img, image_path, city, travel_purpose, companion, rating, deleted_at
) VALUES (
    travel_history_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL
)
""";

        return jdbc.update(sql,
                travel.getUserId(),
                travel.getCountryId(),
                travel.getTitle(),
                travel.getContent(),
                travel.getTravelAt(),
                travel.getTravelImg(),
                travel.getImagePath(),    // 추가된 파일명 파라미터
                travel.getCity(),
                travel.getTravelPurpose(),
                travel.getCompanion(),
                travel.getRating()
        );

    }

    @Override
    public List<TravelHistory> selectByUserId(Long userId) {
        String sql = "SELECT * FROM travel_history WHERE user_id = ? AND deleted_at IS NULL ORDER BY travel_at DESC";

        // 디버깅 로그 추가
        System.out.println("=== DAO 조회 ===");
        System.out.println("SQL: " + sql);
        System.out.println("User ID 파라미터: " + userId);

        List<TravelHistory> result = jdbc.query(sql, rowMapper, userId);
        System.out.println("조회된 레코드 수: " + result.size());

        return result;
    }


    @Override
    public TravelHistory selectById(Long id) {
        String sql = "SELECT * FROM travel_history WHERE travel_history_id = ?";
        try {
            return jdbc.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM travel_history WHERE travel_history_id = ?";
        return jdbc.update(sql, id);
    }

    @Override
    public int update(TravelHistory th) {
        String sql = """
    UPDATE travel_history SET
        country_id = ?, title = ?, content = ?, travel_at = ?,
        travel_img = ?, image_path = ?, city = ?,
        travel_purpose = ?, companion = ?, rating = ?
    WHERE travel_history_id = ? AND user_id = ?
    """;


        return jdbc.update(sql,
                th.getCountryId(),
                th.getTitle(),
                th.getContent(),
                th.getTravelAt(),
                th.getTravelImg(),
                th.getImagePath(),    // 6번째 파라미터: image_path
                th.getCity(),
                th.getTravelPurpose(),
                th.getCompanion(),
                th.getRating(),
                th.getTravelHistoryId(),
                th.getUserId()
        );
    }

    /** 등록한 여행 사진 개수 */
    public int getTravelPhotoCount(Long userId) {
        String sql = """
            SELECT COUNT(*)
              FROM travel_history
             WHERE user_id = ?
               AND travel_img IS NOT NULL
               AND deleted_at IS NULL
        """;
        try {
            return jdbc.queryForObject(sql, Integer.class, userId);
        } catch (Exception e) {
            return 0;
        }
    }
}

