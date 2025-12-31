package org.example.subsidyapi.subsidy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.example.subsidyapi.controller.TotalAmountByStatusResponse;
import org.example.subsidyapi.repository.SubsidyApplicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(SubsidyApplicationRepository.class)
class SubsidyApplicationRepositoryTest {

  @Autowired
  private SubsidyApplicationRepository repository;

  @Test
  void findAll_returnsSeedData() {
    List<SubsidyApplication> list = repository.findAll();

    assertEquals(3, list.size());
    assertEquals("山田太郎", list.get(0).getApplicantName());
  }

  @Test
  void findById_found_returnsApplication() {
    var opt = repository.findById(1L);

    assertTrue(opt.isPresent());
    assertEquals(ApplicationStatus.APPLIED, opt.get().getStatus());
    assertEquals(new BigDecimal("1000000"), opt.get().getAmount());
  }

  @Test
  void findByStatus_returnsOnlyMatchingRows() {
    List<SubsidyApplication> list = repository.findByStatus(ApplicationStatus.APPROVED);

    assertEquals(1, list.size());
    assertEquals("鈴木花子", list.get(0).getApplicantName());
  }

  @Test
  void sumAmount_returnsTotal() {
    BigDecimal total = repository.sumAmount();

    assertEquals(new BigDecimal("2250000"), total);
  }

  @Test
  void sumAmountByStatus_returnsTotal() {
    BigDecimal total = repository.sumAmountByStatus(ApplicationStatus.PAID);

    assertEquals(new BigDecimal("750000"), total);
  }

  @Test
  void sumAmountGroupedByStatus_returnsOrderedTotals() {
    List<TotalAmountByStatusResponse> totals = repository.sumAmountGroupedByStatus();

    assertEquals(3, totals.size());
    assertEquals(ApplicationStatus.APPLIED, totals.get(0).status());
    assertEquals(new BigDecimal("1000000"), totals.get(0).totalAmount());
  }

  @Test
  void insert_success_returnsGeneratedId_andRowIsReadable() {
    long newId = repository.insert(
        "新規 申請者",
        LocalDate.of(2024, 6, 4),
        new BigDecimal("1500000"),
        ApplicationStatus.WITHDRAWN
    );

    assertTrue(newId > 0);
    var opt = repository.findById(newId);
    assertTrue(opt.isPresent());
    assertEquals("新規 申請者", opt.get().getApplicantName());
    assertEquals(ApplicationStatus.WITHDRAWN, opt.get().getStatus());
  }

  @Test
  void update_existingApplication_updatesRow() {
    int updated = repository.update(
        1L,
        "更新 太郎",
        LocalDate.of(2024, 6, 10),
        new BigDecimal("1200000"),
        ApplicationStatus.APPROVED
    );

    assertEquals(1, updated);
    var opt = repository.findById(1L);
    assertTrue(opt.isPresent());
    assertEquals("更新 太郎", opt.get().getApplicantName());
    assertEquals(ApplicationStatus.APPROVED, opt.get().getStatus());
  }

  @Test
  void delete_existingApplication_deletesRow() {
    int deleted = repository.delete(1L);
    assertEquals(1, deleted);
    assertTrue(repository.findById(1L).isEmpty());
  }

  @Test
  void delete_notFound_returns0() {
    int deleted = repository.delete(9999L);
    assertEquals(0, deleted);
  }
}