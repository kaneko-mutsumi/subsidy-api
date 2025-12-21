package org.example.subsidyapi.controller;

import java.math.BigDecimal;
import org.example.subsidyapi.subsidy.ApplicationStatus;

public record TotalAmountByStatusResponse(
    ApplicationStatus status,
    BigDecimal totalAmount
) {}
