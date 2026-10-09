package org.example.subsidyapi.staff;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateStaffUserRequest(
    @NotBlank(message = "name は必須です")
    String name,
    @NotBlank(message = "email は必須です")
    @Email(message = "email の形式が不正です")
    String email,
    @NotBlank(message = "role は必須です（ADMIN / STAFF）")
    String role
) {}
