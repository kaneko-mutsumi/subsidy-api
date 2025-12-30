package org.example.subsidyapi.controller;

public record CreateSubsidyApplicationRequest(
    String applicantName,
    String applicationDate, // "2024-06-01" 形式
    String amount,          // "1000000" 形式
    String status           // "APPLIED" 等
) {}
