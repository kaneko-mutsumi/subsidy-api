package org.example.subsidyapi.staff;

import java.time.LocalDateTime;

public class StaffUser {

  private long id;
  private String name;
  private String email;
  private StaffRole role;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public StaffUser(long id, String name, String email, StaffRole role,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public long getId() { return id; }
  public String getName() { return name; }
  public String getEmail() { return email; }
  public StaffRole getRole() { return role; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
}
