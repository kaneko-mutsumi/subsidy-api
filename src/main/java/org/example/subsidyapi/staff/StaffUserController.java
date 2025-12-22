package org.example.subsidyapi.staff;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffUserController {

  private final StaffUserService service;

  public StaffUserController(StaffUserService service) {
    this.service = service;
  }

  @GetMapping("/staff-users")
  public List<StaffUser> getAllStaffUsers() {
    return service.getAllStaffUsers();
  }

  @GetMapping("/staff-users/{id}")
  public ResponseEntity<StaffUser> getStaffUserById(@PathVariable long id) {
    return service.getStaffUserById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
