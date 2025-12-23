package org.example.subsidyapi.staff;

public record CreateStaffUserRequest(
    String name,
    String email,
    String role
) {}
