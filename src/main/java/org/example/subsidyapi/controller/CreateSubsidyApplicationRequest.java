package org.example.subsidyapi.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateSubsidyApplicationRequest(
    @NotBlank(message = "applicantName は必須です")
    String applicantName,
    @NotBlank(message = "applicationDate は必須です（例: 2024-06-01）")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "applicationDate の形式が不正です（例: 2024-06-01）")
    String applicationDate, // "2024-06-01" 形式
    @NotBlank(message = "amount は必須です（例: 1000000）")
    @Pattern(regexp = "\\d+", message = "amount の形式が不正です（例: 1000000）")
    String amount,          // "1000000" 形式
    @NotBlank(message = "status は必須です（APPLIED / APPROVED / PAID / WITHDRAWN）")
    String status           // "APPLIED" 等
) {}
