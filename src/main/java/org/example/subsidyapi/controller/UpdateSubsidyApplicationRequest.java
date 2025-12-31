package org.example.subsidyapi.controller;

import jakarta.validation.constraints.NotBlank;

public record UpdateSubsidyApplicationRequest(
    @NotBlank(message = "applicantName は必須です")
    String applicantName,
    @NotBlank(message = "applicationDate は必須です（例: 2024-06-01）")
    String applicationDate,
    @NotBlank(message = "amount は必須です（例: 1000000）")
    String amount,
    @NotBlank(message = "status は必須です（APPLIED / APPROVED / PAID / WITHDRAWN）")
    String status
) {}