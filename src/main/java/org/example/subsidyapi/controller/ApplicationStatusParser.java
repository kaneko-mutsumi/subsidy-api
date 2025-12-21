package org.example.subsidyapi.controller;

import org.example.subsidyapi.subsidy.ApplicationStatus;

public class ApplicationStatusParser {

  private ApplicationStatusParser() {
    // new されないため（ユーティリティ扱い）
  }

  public static ApplicationStatus parse(String status) {
    try {
      return ApplicationStatus.valueOf(status.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new InvalidRequestParameterException(
          "status の値が不正です: " + status + "（APPLIED / APPROVED / PAID / WITHDRAWN を指定）"
      );
    }
  }
}
