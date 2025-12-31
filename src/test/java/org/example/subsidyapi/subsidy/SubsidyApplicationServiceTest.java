package org.example.subsidyapi.subsidy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.subsidyapi.controller.TotalAmountByStatusResponse;
import org.example.subsidyapi.repository.SubsidyApplicationRepository;
import org.example.subsidyapi.service.SubsidyApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubsidyApplicationServiceTest {

  @Mock
  private SubsidyApplicationRepository repository;

  @InjectMocks
  private SubsidyApplicationService service;

  @Test
  void getAllApplications_returnsRepositoryResult() {
    SubsidyApplication a1 = new SubsidyApplication(
        1L,
        "A",
        LocalDate.of(2024, 6, 1),
        new BigDecimal("100000"),
        ApplicationStatus.APPLIED
    );
    SubsidyApplication a2 = new SubsidyApplication(
        2L,
        "B",
        LocalDate.of(2024, 6, 2),
        new BigDecimal("200000"),
        ApplicationStatus.APPROVED
    );

    when(repository.findAll()).thenReturn(List.of(a1, a2));

    List<SubsidyApplication> result = service.getAllApplications();

    assertEquals(2, result.size());
    assertSame(a1, result.get(0));
    assertSame(a2, result.get(1));
    verify(repository).findAll();
  }

  @Test
  void getApplicationById_returnsOptional() {
    SubsidyApplication app = new SubsidyApplication(
        10L,
        "申請者",
        LocalDate.of(2024, 6, 1),
        new BigDecimal("500000"),
        ApplicationStatus.PAID
    );

    when(repository.findById(10L)).thenReturn(Optional.of(app));

    Optional<SubsidyApplication> result = service.getApplicationById(10L);

    assertTrue(result.isPresent());
    assertSame(app, result.get());
    verify(repository).findById(10L);
  }

  @Test
  void getApplicationsByStatus_callsRepository() {
    when(repository.findByStatus(ApplicationStatus.APPLIED)).thenReturn(List.of());

    service.getApplicationsByStatus(ApplicationStatus.APPLIED);

    verify(repository).findByStatus(ApplicationStatus.APPLIED);
  }

  @Test
  void getTotalAmount_returnsRepositoryTotal() {
    when(repository.sumAmount()).thenReturn(new BigDecimal("123"));

    BigDecimal total = service.getTotalAmount();

    assertEquals(new BigDecimal("123"), total);
    verify(repository).sumAmount();
  }

  @Test
  void getTotalAmountByStatus_returnsRepositoryTotal() {
    when(repository.sumAmountByStatus(ApplicationStatus.APPROVED))
        .thenReturn(new BigDecimal("456"));

    BigDecimal total = service.getTotalAmountByStatus(ApplicationStatus.APPROVED);

    assertEquals(new BigDecimal("456"), total);
    verify(repository).sumAmountByStatus(ApplicationStatus.APPROVED);
  }

  @Test
  void getTotalAmountByStatusList_returnsRepositoryResult() {
    List<TotalAmountByStatusResponse> list = List.of(
        new TotalAmountByStatusResponse(ApplicationStatus.APPLIED, new BigDecimal("100"))
    );

    when(repository.sumAmountGroupedByStatus()).thenReturn(list);

    List<TotalAmountByStatusResponse> result = service.getTotalAmountByStatusList();

    assertSame(list, result);
    verify(repository).sumAmountGroupedByStatus();
  }

  @Test
  void createApplication_success_returnsCreated() {
    long newId = 100L;
    SubsidyApplication created = new SubsidyApplication(
        newId,
        "新規",
        LocalDate.of(2024, 6, 5),
        new BigDecimal("300000"),
        ApplicationStatus.APPLIED
    );

    when(repository.insert(
        "新規",
        LocalDate.of(2024, 6, 5),
        new BigDecimal("300000"),
        ApplicationStatus.APPLIED
    )).thenReturn(newId);
    when(repository.findById(newId)).thenReturn(Optional.of(created));

    SubsidyApplication result = service.createApplication(
        "新規",
        LocalDate.of(2024, 6, 5),
        new BigDecimal("300000"),
        ApplicationStatus.APPLIED
    );

    assertSame(created, result);
    verify(repository).insert(
        "新規",
        LocalDate.of(2024, 6, 5),
        new BigDecimal("300000"),
        ApplicationStatus.APPLIED
    );
    verify(repository).findById(newId);
  }

  @Test
  void createApplication_insertSucceededButNotFound_throwsIllegalStateException() {
    long newId = 200L;
    when(repository.insert("X", LocalDate.of(2024, 6, 1), new BigDecimal("1"),
        ApplicationStatus.APPLIED)).thenReturn(newId);
    when(repository.findById(newId)).thenReturn(Optional.empty());

    IllegalStateException ex = assertThrows(
        IllegalStateException.class,
        () -> service.createApplication(
            "X",
            LocalDate.of(2024, 6, 1),
            new BigDecimal("1"),
            ApplicationStatus.APPLIED)
    );

    assertEquals("Insert succeeded but not found: id=200", ex.getMessage());
  }

  @Test
  void updateApplication_whenTargetNotFound_returnsEmpty() {
    when(repository.update(
        999L,
        "X",
        LocalDate.of(2024, 6, 1),
        new BigDecimal("1"),
        ApplicationStatus.APPLIED
    )).thenReturn(0);

    Optional<SubsidyApplication> result = service.updateApplication(
        999L,
        "X",
        LocalDate.of(2024, 6, 1),
        new BigDecimal("1"),
        ApplicationStatus.APPLIED
    );

    assertTrue(result.isEmpty());
    verify(repository).update(
        999L,
        "X",
        LocalDate.of(2024, 6, 1),
        new BigDecimal("1"),
        ApplicationStatus.APPLIED
    );
    verify(repository, never()).findById(anyLong());
  }

  @Test
  void updateApplication_updateSucceededButNotFound_throwsIllegalStateException() {
    when(repository.update(
        1L,
        "Y",
        LocalDate.of(2024, 6, 2),
        new BigDecimal("2"),
        ApplicationStatus.APPROVED
    )).thenReturn(1);
    when(repository.findById(1L)).thenReturn(Optional.empty());

    IllegalStateException ex = assertThrows(
        IllegalStateException.class,
        () -> service.updateApplication(
            1L,
            "Y",
            LocalDate.of(2024, 6, 2),
            new BigDecimal("2"),
            ApplicationStatus.APPROVED)
    );

    assertEquals("Update succeeded but not found: id=1", ex.getMessage());
  }

  @Test
  void deleteApplication_whenDeleted_returnsTrue() {
    when(repository.delete(1L)).thenReturn(1);

    boolean result = service.deleteApplication(1L);

    assertTrue(result);
    verify(repository).delete(1L);
  }

  @Test
  void deleteApplication_whenNotFound_returnsFalse() {
    when(repository.delete(999L)).thenReturn(0);

    boolean result = service.deleteApplication(999L);

    assertFalse(result);
    verify(repository).delete(999L);
  }
}