package org.example.subsidyapi.controller;

public record UpdateSubsidyApplicationRequest(
    String applicantName,
    String applicationDate,
    String amount,
    String status
) {}
