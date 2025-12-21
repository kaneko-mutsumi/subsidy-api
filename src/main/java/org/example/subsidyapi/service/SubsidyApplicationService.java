package org.example.subsidyapi.service;

import java.math.BigDecimal;
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

  /**
   * ステータス別の申請額合計リストを取得して返します。
   *
   * @return status と totalAmount を持つ集計結果のリスト
   */
  public List<TotalAmountByStatusResponse> getTotalAmountByStatusList() {
    return repository.sumAmountGroupedByStatus();
  }
}
