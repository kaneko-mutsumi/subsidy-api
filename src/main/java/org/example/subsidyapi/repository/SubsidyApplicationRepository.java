package org.example.subsidyapi.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.subsidyapi.controller.TotalAmountByStatusResponse;
import org.example.subsidyapi.subsidy.ApplicationStatus;
import org.example.subsidyapi.subsidy.SubsidyApplication;
import org.springframework.stereotype.Repository;

@Repository
public class SubsidyApplicationRepository {

  private final SubsidyApplicationMapper mapper;

  public SubsidyApplicationRepository(SubsidyApplicationMapper mapper) {
    this.mapper = mapper;
  }

  public List<SubsidyApplication> findAll() {
    return mapper.findAll();
  }

  public Optional<SubsidyApplication> findById(long id) {
    return Optional.ofNullable(
        mapper.findById(id));                           // Optional<SubsidyApplication>
  }

  public List<SubsidyApplication> findByStatus(ApplicationStatus status) {
    return mapper.findByStatus(status);
  }

  public BigDecimal sumAmount() {
    return mapper.sumAmount();
  }

  public BigDecimal sumAmountByStatus(ApplicationStatus status) {
    return mapper.sumAmountByStatus(status);
  }

  public List<TotalAmountByStatusResponse> sumAmountGroupedByStatus() {
    return mapper.sumAmountGroupedByStatus();
  }

  public long insert(String applicantName, LocalDate applicationDate, BigDecimal amount,
      ApplicationStatus status) {
    SubsidyApplicationCreateCommand command =
        new SubsidyApplicationCreateCommand(applicantName, applicationDate, amount, status);
    mapper.insert(command);
    Long id = command.getId();
    if (id == null) {
      throw new IllegalStateException("Failed to retrieve generated id");
    }
    return id;
  }

  public int update(long id, String applicantName, LocalDate applicationDate, BigDecimal amount,
      ApplicationStatus status) {
    return mapper.update(id, applicantName, applicationDate, amount, status);
  }

  public int delete(long id) {
    return mapper.delete(id);
  }
}
