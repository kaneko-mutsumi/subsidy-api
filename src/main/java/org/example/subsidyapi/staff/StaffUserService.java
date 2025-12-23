package org.example.subsidyapi.staff;

import java.util.List;
import org.springframework.dao.DuplicateKeyException;
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

  public StaffUser createStaffUser(String name, String email, StaffRole role) {
    try {
      long newId = repository.insert(name, email, role); // 採番idを受け取る
      return repository.findById(newId)
          .orElseThrow(
              () -> new IllegalStateException("Insert succeeded but not found: id=" + newId));
    } catch (DuplicateKeyException e) {
      throw new EmailAlreadyExistsException("email はすでに登録されています: " + email);
    }
  }
}
