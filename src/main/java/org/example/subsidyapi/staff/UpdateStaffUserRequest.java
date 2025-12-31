package org.example.subsidyapi.staff;

import jakarta.validation.constraints.NotBlank;

public record UpdateStaffUserRequest(
    @NotBlank(message = "name は必須です")
    String name,
    @NotBlank(message = "email は必須です")
    String email,
    @NotBlank(message = "role は必須です（ADMIN / STAFF）")
    String role
) {}