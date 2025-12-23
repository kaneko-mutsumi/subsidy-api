package org.example.subsidyapi.staff;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StaffUserService {

  private final StaffUserRepository repository;

  public StaffUserService(StaffUserRepository repository) {
    this.repository = repository;
  }

  public List<StaffUser> getAllStaffUsers() {
    return repository.findAll();
  }

  public java.util.Optional<StaffUser> getStaffUserById(long id) {
    return repository.findById(id);
  }

  public List<StaffUser> getStaffUsersByRole(StaffRole role) {
    return repository.findByRole(role);
  }
}
