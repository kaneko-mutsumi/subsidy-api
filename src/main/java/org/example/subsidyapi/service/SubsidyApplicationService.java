package org.example.subsidyapi.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.subsidyapi.controller.TotalAmountByStatusResponse;
import org.example.subsidyapi.repository.SubsidyApplicationRepository;
import org.example.subsidyapi.subsidy.ApplicationStatus;
import org.example.subsidyapi.subsidy.SubsidyApplication;
import org.springframework.stereotype.Service;

@Service
public class SubsidyApplicationService {

  private final SubsidyApplicationRepository repository;

  public SubsidyApplicationService(SubsidyApplicationRepository repository) {
    this.repository = repository;
  }

  public List<SubsidyApplication> getAllApplications() {
    return repository.findAll();
  }

  public Optional<SubsidyApplication> getApplicationById(long id) {
    return repository.findById(id);
  }

  public List<SubsidyApplication> getApplicationsByStatus(ApplicationStatus status) {
    return repository.findByStatus(status);
  }

  public BigDecimal getTotalAmount() {
    return repository.sumAmount();
  }

  public BigDecimal getTotalAmountByStatus(ApplicationStatus status) {
    return repository.sumAmountByStatus(status);
  }

  public List<TotalAmountByStatusResponse> getTotalAmountByStatusList() {
    return repository.sumAmountGroupedByStatus();
  }

  // ===== ここから追加（CRUDのC/U/D）=====

  public SubsidyApplication createApplication(
      String applicantName,
      LocalDate applicationDate,
      BigDecimal amount,
      ApplicationStatus status
  ) {
    long newId = repository.insert(applicantName, applicationDate, amount, status);
    return repository.findById(newId)
        .orElseThrow(() -> new IllegalStateException("Insert succeeded but not found: id=" + newId));
  }

  public Optional<SubsidyApplication> updateApplication(
      long id,
      String applicantName,
      LocalDate applicationDate,
      BigDecimal amount,
      ApplicationStatus status
  ) {
    int updated = repository.update(id, applicantName, applicationDate, amount, status);
    if (updated == 0) {
      return Optional.empty();
    }
    return repository.findById(id)
        .or(() -> { throw new IllegalStateException("Update succeeded but not found: id=" + id); });
  }

  public boolean deleteApplication(long id) {
    int deleted = repository.delete(id);
    return deleted > 0;
  }
}
