package org.example.subsidyapi.controller;

import java.util.List;
import org.example.subsidyapi.service.SubsidyApplicationService;
import org.example.subsidyapi.subsidy.ApplicationStatus;
import org.example.subsidyapi.subsidy.SubsidyApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubsidyApplicationController {

  private final SubsidyApplicationService service;

  public SubsidyApplicationController(SubsidyApplicationService service) {
    this.service = service;
  }

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

  @GetMapping("/applications/{id}")
  public ResponseEntity<SubsidyApplication> getApplicationById(@PathVariable long id) {
    return service.getApplicationById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
