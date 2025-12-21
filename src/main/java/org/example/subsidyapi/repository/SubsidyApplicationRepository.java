package org.example.subsidyapi.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.subsidyapi.controller.TotalAmountByStatusResponse;
import org.example.subsidyapi.subsidy.ApplicationStatus;
import org.example.subsidyapi.subsidy.SubsidyApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@Repository
public class SubsidyApplicationRepository {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<SubsidyApplication> rowMapper = (rs, rowNum) -> {
    long id = rs.getLong("id");
    String applicantName = rs.getString("applicant_name");
    LocalDate applicationDate = rs.getDate("application_date").toLocalDate();
    BigDecimal amount = rs.getBigDecimal("amount");

    String statusString = rs.getString("status");
    ApplicationStatus status = ApplicationStatus.valueOf(statusString.trim().toUpperCase());

    return new SubsidyApplication(id, applicantName, applicationDate, amount, status);
  };

  public SubsidyApplicationRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<SubsidyApplication> findAll() {
    String sql = """
        SELECT id, applicant_name, application_date, amount, status
        FROM subsidy_applications
        ORDER BY id
        """;
    return jdbcTemplate.query(sql, rowMapper);
  }

  public Optional<SubsidyApplication> findById(long id) {
    String sql = """
        SELECT id, applicant_name, application_date, amount, status
        FROM subsidy_applications
        WHERE id = ?
        """;

    return jdbcTemplate.query(sql, rowMapper, id)   // List<SubsidyApplication>
        .stream()                                  // Stream<SubsidyApplication>
        .findFirst();                              // Optional<SubsidyApplication>
  }

  public List<SubsidyApplication> findByStatus(ApplicationStatus status) {
    String sql = """
        SELECT id, applicant_name, application_date, amount, status
        FROM subsidy_applications
        WHERE status = ?
        ORDER BY id
        """;

    return jdbcTemplate.query(sql, rowMapper, status.name());
  }

  public BigDecimal sumAmount() {
    String sql = """
        SELECT COALESCE(SUM(amount), 0) AS total_amount
        FROM subsidy_applications
        """;
    return jdbcTemplate.queryForObject(sql, BigDecimal.class);
  }

  public BigDecimal sumAmountByStatus(ApplicationStatus status) {
    String sql = """
        SELECT COALESCE(SUM(amount), 0) AS total_amount
        FROM subsidy_applications
        WHERE status = ?
        """;
    return jdbcTemplate.queryForObject(sql, BigDecimal.class, status.name());
  }

  /**
   * subsidy_applications を status ごとに GROUP BY して amount 合計を集計します（NULLは0扱い）。
   *
   * @return status 別の合計金額（totalAmount）のリスト
   */
  public List<TotalAmountByStatusResponse> sumAmountGroupedByStatus() {
    String sql = """
        SELECT
          status,
          COALESCE(SUM(amount), 0) AS total_amount
        FROM subsidy_applications
        GROUP BY status
        ORDER BY status
        """;

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
      // 1) status列をStringで取得
      String statusString = rs.getString("status");

      // 2) trim + toUpperCase して ApplicationStatus に変換
      ApplicationStatus status = ApplicationStatus.valueOf(
          statusString.trim().toUpperCase()
      );

      // 3) total_amount を BigDecimal で取得
      BigDecimal totalAmount = rs.getBigDecimal("total_amount");

      // 4) レスポンス用recordを作って返す
      return new TotalAmountByStatusResponse(status, totalAmount);
    });
  }
}
