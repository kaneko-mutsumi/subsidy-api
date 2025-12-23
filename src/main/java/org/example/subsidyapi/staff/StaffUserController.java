package org.example.subsidyapi.staff;

import java.util.List;
import org.example.subsidyapi.controller.InvalidRequestParameterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffUserController {

  private final StaffUserService service;

  public StaffUserController(StaffUserService service) {
    this.service = service;
  }

  @GetMapping("/staff-users")
  public List<StaffUser> getStaffUsers(@RequestParam(required = false) String role) {
    if (role == null) {
      return service.getAllStaffUsers();
    }
    StaffRole r = parseRole(role);
    return service.getStaffUsersByRole(r);
  }

  private StaffRole parseRole(String role) {
    try {
      return StaffRole.valueOf(role.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new InvalidRequestParameterException(
          "role の値が不正です: " + role + "（ADMIN / STAFF を指定）"
      );
    }
  }

  @GetMapping("/staff-users/{id}")
  public ResponseEntity<StaffUser> getStaffUserById(@PathVariable long id) {
    return service.getStaffUserById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("/staff-users")
  public ResponseEntity<StaffUser> createStaffUser(@RequestBody CreateStaffUserRequest req) {
    StaffRole role = parseRole(req.role());

    StaffUser created = service.createStaffUser(req.name(), req.email(), role);

    return ResponseEntity.status(201).body(created);
  }
}
