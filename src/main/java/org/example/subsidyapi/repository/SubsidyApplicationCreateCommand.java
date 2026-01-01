package org.example.subsidyapi.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.subsidyapi.subsidy.ApplicationStatus;

public class SubsidyApplicationCreateCommand {

  private Long id;
  private final String applicantName;
  private final LocalDate applicationDate;
  private final BigDecimal amount;
  private final ApplicationStatus status;

  public SubsidyApplicationCreateCommand(String applicantName, LocalDate applicationDate,
      BigDecimal amount, ApplicationStatus status) {
    this.applicantName = applicantName;
    this.applicationDate = applicationDate;
    this.amount = amount;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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