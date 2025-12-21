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
}
