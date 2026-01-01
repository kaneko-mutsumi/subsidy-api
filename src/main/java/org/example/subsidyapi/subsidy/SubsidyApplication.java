package org.example.subsidyapi.subsidy;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SubsidyApplication {

  // フィールド（1件の申請が持つ情報）
  private long id;
  private String applicantName;
  private LocalDate applicationDate;
  private BigDecimal amount;
  private ApplicationStatus status;

  /**
   * MyBatis の constructor マッピングが Long を渡してくる場合に備えたコンストラクタ。
   * （SubsidyApplication(Long, ...) が無いと NoSuchMethodException になるため）
   */
  public SubsidyApplication(
      Long id,
      String applicantName,
      LocalDate applicationDate,
      BigDecimal amount,
      ApplicationStatus status
  ) {
    this(
        id == null ? 0L : id,
        applicantName,
        applicationDate,
        amount,
        status
    );
  }

  // 既存：全部のフィールドを受け取るコンストラクタ（id は long）
  public SubsidyApplication(
      long id,
      String applicantName,
      LocalDate applicationDate,
      BigDecimal amount,
      ApplicationStatus status
  ) {
    this.id = id;
    this.applicantName = applicantName;
    this.applicationDate = applicationDate;
    this.amount = amount;
    this.status = status;
  }

  // getter メソッド
  public long getId() {
    return id;
  }

  public String getApplicantName() {
    return applicantName;
  }

  public LocalDate getApplicationDate() {
    return applicationDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public ApplicationStatus getStatus() {
    return status;
  }
}
