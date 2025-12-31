package org.example.subsidyapi.controller;

import java.util.List;

import jakarta.validation.Valid;
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
      @Valid @RequestBody CreateSubsidyApplicationRequest req
  ) {
    SubsidyApplication created =
        service.createApplication(
            req.applicantName(),
            req.applicationDate(),
            req.amount(),
            req.status());

    return ResponseEntity.status(201).body(created);
  }

  // ===== U: Update =====
  @PutMapping("/applications/{id}")
  public ResponseEntity<SubsidyApplication> updateApplication(
      @PathVariable long id,
      @Valid @RequestBody UpdateSubsidyApplicationRequest req
  ) {
    return service.updateApplication(
            id,
            req.applicantName(),
            req.applicationDate(),
            req.amount(),
            req.status())
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
}

