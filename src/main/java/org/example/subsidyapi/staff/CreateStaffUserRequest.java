package org.example.subsidyapi.staff;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateStaffUserRequest(
    @NotBlank(message = "name は必須です")
    @Size(max = 100, message = "name は100文字以内で入力してください")
    String name,
    @NotBlank(message = "email は必須です")
    @Email(message = "email の形式が不正です")
    @Size(max = 100, message = "email は100文字以内で入力してください")
    String email,
    @NotBlank(message = "role は必須です（ADMIN / STAFF）")
    String role
) {}