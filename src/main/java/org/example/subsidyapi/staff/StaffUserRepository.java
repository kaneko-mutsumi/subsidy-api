package org.example.subsidyapi.staff;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

  public Optional<StaffUser> findById(long id) {
    String sql = """
        SELECT id, name, email, role, created_at, updated_at
        FROM staff_users
        WHERE id = ?
        """;

    return jdbcTemplate.query(sql, rowMapper, id)
        .stream()
        .findFirst();
  }

  public List<StaffUser> findByRole(StaffRole role) {
    String sql = """
        SELECT id, name, email, role, created_at, updated_at
        FROM staff_users
        WHERE role = ?
        ORDER BY id
        """;
    return jdbcTemplate.query(sql, rowMapper, role.name());
  }

  public int insert(StaffUser user) {
    String sql = """
        INSERT INTO staff_users (id, name, email, role)
        VALUES (?, ?, ?, ?)
        """;
    return jdbcTemplate.update(
        sql,
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getRole().name()
    );
  }

  public long insert(String name, String email, StaffRole role) {
    String sql = """
        INSERT INTO staff_users (name, email, role)
        VALUES (?, ?, ?)
        """;

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
      ps.setString(1, name);
      ps.setString(2, email);
      ps.setString(3, role.name());
      return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    if (key == null) {
      throw new IllegalStateException("Failed to retrieve generated id");
    }
    return key.longValue();
  }

  public int update(long id, String name, String email, StaffRole role) {
    String sql = """
      UPDATE staff_users
      SET name = ?, email = ?, role = ?, updated_at = CURRENT_TIMESTAMP
      WHERE id = ?
      """;
    return jdbcTemplate.update(sql, name, email, role.name(), id);
  }

  public int delete(long id) {
    String sql = "DELETE FROM staff_users WHERE id = ?";
    return jdbcTemplate.update(sql, id);
  }
}
