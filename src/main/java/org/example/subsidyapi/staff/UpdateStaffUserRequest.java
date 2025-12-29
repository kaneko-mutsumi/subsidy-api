package org.example.subsidyapi.staff;

public record UpdateStaffUserRequest(
    String name,
    String email,
    String role
) {}
