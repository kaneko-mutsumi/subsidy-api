package org.example.subsidyapi.staff;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StaffUserRepository {

  private final JdbcTemplate jdbcTemplate;

  public StaffUserRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<StaffUser> rowMapper = (rs, rowNum) -> {
    long id = rs.getLong("id");
    String name = rs.getString("name");
    String email = rs.getString("email");

    String roleString = rs.getString("role");
    StaffRole role = StaffRole.valueOf(roleString.trim().toUpperCase());

    LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);

    LocalDateTime updatedAt = rs.getObject("updated_at", LocalDateTime.class);

    return new StaffUser(id, name, email, role, createdAt, updatedAt);
  };

  public List<StaffUser> findAll() {
    String sql = """
        SELECT id, name, email, role, created_at, updated_at
        FROM staff_users
        ORDER BY id
        """;
    return jdbcTemplate.query(sql, rowMapper);
  }

  public java.util.Optional<StaffUser> findById(long id) {
    String sql = """
      SELECT id, name, email, role, created_at, updated_at
      FROM staff_users
      WHERE id = ?
      """;

    return jdbcTemplate.query(sql, rowMapper, id)
        .stream()
        .findFirst();
  }
}
