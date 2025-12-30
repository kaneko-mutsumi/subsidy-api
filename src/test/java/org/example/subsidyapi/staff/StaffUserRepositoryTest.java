package org.example.subsidyapi.staff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;

@JdbcTest
@Import(StaffUserRepository.class)
class StaffUserRepositoryTest {

  @Autowired
  private StaffUserRepository repository;

  @Test
  void findAll_returnsSeedData() {
    List<StaffUser> list = repository.findAll();

    // data.sql の staff_users が3件入っている前提
    assertEquals(3, list.size());
    assertEquals("admin@example.com", list.get(0).getEmail());
  }

  @Test
  void findById_found_returnsUser() {
    var opt = repository.findById(1L);

    assertTrue(opt.isPresent());
    assertEquals("admin@example.com", opt.get().getEmail());
    assertEquals(StaffRole.ADMIN, opt.get().getRole());
  }

  @Test
  void findById_notFound_returnsEmpty() {
    var opt = repository.findById(9999L);
    assertTrue(opt.isEmpty());
  }

  @Test
  void findByRole_staff_returnsOnlyStaff() {
    List<StaffUser> list = repository.findByRole(StaffRole.STAFF);

    assertEquals(2, list.size());
    assertTrue(list.stream().allMatch(u -> u.getRole() == StaffRole.STAFF));
  }

  @Test
  void insert_success_returnsGeneratedId_andRowIsReadable() {
    long newId = repository.insert("追加 職員", "new@example.com", StaffRole.STAFF);

    assertTrue(newId > 0);

    var opt = repository.findById(newId);
    assertTrue(opt.isPresent());
    assertEquals("new@example.com", opt.get().getEmail());
    assertEquals("追加 職員", opt.get().getName());
    assertEquals(StaffRole.STAFF, opt.get().getRole());
  }

  @Test
  void insert_duplicateEmail_throwsDuplicateKeyException() {
    // data.sql に admin@example.com がいる前提
    assertThrows(DuplicateKeyException.class,
        () -> repository.insert("重複", "admin@example.com", StaffRole.STAFF));
  }

  @Test
  void update_existingUser_updatesRow() {
    int updated = repository.update(1L, "更新 太郎", "admin_updated@example.com", StaffRole.ADMIN);
    assertEquals(1, updated);

    var opt = repository.findById(1L);
    assertTrue(opt.isPresent());
    assertEquals("更新 太郎", opt.get().getName());
    assertEquals("admin_updated@example.com", opt.get().getEmail());
  }

  @Test
  void delete_existingUser_deletesRow() {
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
