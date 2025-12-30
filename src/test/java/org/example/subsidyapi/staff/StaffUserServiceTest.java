package org.example.subsidyapi.staff;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DuplicateKeyException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StaffUserServiceTest {

  @Mock
  private StaffUserRepository repository;

  @InjectMocks
  private StaffUserService service;

  @Test
  void getAllStaffUsers_returnsRepositoryResult() {
    StaffUser u1 = new StaffUser(1L, "A", "a@example.com", StaffRole.ADMIN, LocalDateTime.now(), null);
    StaffUser u2 = new StaffUser(2L, "B", "b@example.com", StaffRole.STAFF, LocalDateTime.now(), null);
    when(repository.findAll()).thenReturn(List.of(u1, u2));

    List<StaffUser> result = service.getAllStaffUsers();

    assertEquals(2, result.size());
    assertSame(u1, result.get(0));
    assertSame(u2, result.get(1));
    verify(repository).findAll();
  }

  @Test
  void getStaffUserById_returnsOptional() {
    StaffUser u = new StaffUser(10L, "Taro", "t@example.com", StaffRole.STAFF, LocalDateTime.now(), null);
    when(repository.findById(10L)).thenReturn(Optional.of(u));

    Optional<StaffUser> result = service.getStaffUserById(10L);

    assertTrue(result.isPresent());
    assertSame(u, result.get());
    verify(repository).findById(10L);
  }

  @Test
  void getStaffUsersByRole_callsRepository() {
    when(repository.findByRole(StaffRole.STAFF)).thenReturn(List.of());

    service.getStaffUsersByRole(StaffRole.STAFF);

    verify(repository).findByRole(StaffRole.STAFF);
  }

  @Test
  void createStaffUser_success_returnsCreatedUser() {
    long newId = 100L;
    StaffUser created = new StaffUser(newId, "職員 太郎", "ok@example.com", StaffRole.STAFF, LocalDateTime.now(), null);

    when(repository.insert("職員 太郎", "ok@example.com", StaffRole.STAFF)).thenReturn(newId);
    when(repository.findById(newId)).thenReturn(Optional.of(created));

    StaffUser result = service.createStaffUser("職員 太郎", "ok@example.com", StaffRole.STAFF);

    assertSame(created, result);
    verify(repository).insert("職員 太郎", "ok@example.com", StaffRole.STAFF);
    verify(repository).findById(newId);
  }

  @Test
  void createStaffUser_whenEmailDuplicate_throwsEmailAlreadyExistsException() {
    when(repository.insert("重複", "dup@example.com", StaffRole.STAFF))
        .thenThrow(new DuplicateKeyException("duplicate"));

    EmailAlreadyExistsException ex = assertThrows(
        EmailAlreadyExistsException.class,
        () -> service.createStaffUser("重複", "dup@example.com", StaffRole.STAFF)
    );

    assertEquals("email はすでに登録されています: dup@example.com", ex.getMessage());
    verify(repository).insert("重複", "dup@example.com", StaffRole.STAFF);
    verify(repository, never()).findById(anyLong());
  }

  @Test
  void createStaffUser_insertSucceededButNotFound_throwsIllegalStateException() {
    long newId = 200L;
    when(repository.insert("X", "x@example.com", StaffRole.ADMIN)).thenReturn(newId);
    when(repository.findById(newId)).thenReturn(Optional.empty());

    IllegalStateException ex = assertThrows(
        IllegalStateException.class,
        () -> service.createStaffUser("X", "x@example.com", StaffRole.ADMIN)
    );

    assertEquals("Insert succeeded but not found: id=200", ex.getMessage());
    verify(repository).insert("X", "x@example.com", StaffRole.ADMIN);
    verify(repository).findById(newId);
  }

  @Test
  void updateStaffUser_whenTargetNotFound_returnsEmpty() {
    when(repository.update(999L, "X", "x@example.com", StaffRole.ADMIN)).thenReturn(0);

    Optional<StaffUser> result = service.updateStaffUser(999L, "X", "x@example.com", StaffRole.ADMIN);

    assertTrue(result.isEmpty());
    verify(repository).update(999L, "X", "x@example.com", StaffRole.ADMIN);
    verify(repository, never()).findById(anyLong());
  }

  @Test
  void deleteStaffUser_whenDeleted_returnsTrue() {
    when(repository.delete(1L)).thenReturn(1);

    boolean result = service.deleteStaffUser(1L);

    assertTrue(result);
    verify(repository).delete(1L);
  }

  @Test
  void deleteStaffUser_whenNotFound_returnsFalse() {
    when(repository.delete(999L)).thenReturn(0);

    boolean result = service.deleteStaffUser(999L);

    assertFalse(result);
    verify(repository).delete(999L);
  }
}
