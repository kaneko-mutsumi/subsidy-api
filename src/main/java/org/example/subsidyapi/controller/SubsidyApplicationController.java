package org.example.subsidyapi.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.example.subsidyapi.service.SubsidyApplicationService;
import org.example.subsidyapi.subsidy.ApplicationStatus;
import org.example.subsidyapi.subsidy.SubsidyApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubsidyApplicationController {

  private final SubsidyApplicationService service;

  public SubsidyApplicationController(SubsidyApplicationService service) {
    this.service = service;
  }

  // ===== R: List =====
  @GetMapping("/applications")
  public List<SubsidyApplication> getApplications(
      @RequestParam(required = false) String status
  ) {
    if (status == null) {
      return service.getAllApplications();
    }
    ApplicationStatus st = ApplicationStatusParser.parse(status);
    return service.getApplicationsByStatus(st);
  }

  // ===== R: Get by id =====
  @GetMapping("/applications/{id}")
  public ResponseEntity<SubsidyApplication> getApplicationById(@PathVariable long id) {
    return service.getApplicationById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // ===== C: Create =====
  @PostMapping("/applications")
  public ResponseEntity<SubsidyApplication> createApplication(
      @RequestBody CreateSubsidyApplicationRequest req
  ) {
    validateCreateOrUpdate(req.applicantName(), req.applicationDate(), req.amount(), req.status());

    LocalDate date = parseDate(req.applicationDate());
    BigDecimal amount = parseAmount(req.amount());
    ApplicationStatus status = ApplicationStatusParser.parse(req.status());

    SubsidyApplication created =
        service.createApplication(req.applicantName(), date, amount, status);

    return ResponseEntity.status(201).body(created);
  }

  // ===== U: Update =====
  @PutMapping("/applications/{id}")
  public ResponseEntity<SubsidyApplication> updateApplication(
      @PathVariable long id,
      @RequestBody UpdateSubsidyApplicationRequest req
  ) {
    validateCreateOrUpdate(req.applicantName(), req.applicationDate(), req.amount(), req.status());

    LocalDate date = parseDate(req.applicationDate());
    BigDecimal amount = parseAmount(req.amount());
    ApplicationStatus status = ApplicationStatusParser.parse(req.status());

    return service.updateApplication(id, req.applicantName(), date, amount, status)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // ===== D: Delete =====
  @DeleteMapping("/applications/{id}")
  public ResponseEntity<Void> deleteApplication(@PathVariable long id) {
    boolean deleted = service.deleteApplication(id);
    if (!deleted) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build(); // 204
  }

  // ===== validation / parse helpers =====

  private void validateCreateOrUpdate(String applicantName, String applicationDate, String amount, String status) {
    if (applicantName == null || applicantName.isBlank()) {
      throw new InvalidRequestParameterException("applicantName は必須です");
    }
    if (applicationDate == null || applicationDate.isBlank()) {
      throw new InvalidRequestParameterException("applicationDate は必須です（例: 2024-06-01）");
    }
    if (amount == null || amount.isBlank()) {
      throw new InvalidRequestParameterException("amount は必須です（例: 1000000）");
    }
    if (status == null || status.isBlank()) {
      throw new InvalidRequestParameterException("status は必須です（APPLIED / APPROVED / PAID / WITHDRAWN）");
    }
  }

  private LocalDate parseDate(String s) {
    try {
      return LocalDate.parse(s.trim());
    } catch (DateTimeParseException e) {
      throw new InvalidRequestParameterException("applicationDate の形式が不正です: " + s + "（例: 2024-06-01）");
    }
  }

  private BigDecimal parseAmount(String s) {
    try {
      return new BigDecimal(s.trim());
    } catch (NumberFormatException e) {
      throw new InvalidRequestParameterException("amount の形式が不正です: " + s + "（例: 1000000）");
    }
  }
}
