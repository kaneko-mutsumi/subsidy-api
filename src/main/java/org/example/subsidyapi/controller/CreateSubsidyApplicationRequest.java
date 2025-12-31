package org.example.subsidyapi.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.subsidyapi.subsidy.ApplicationStatus;

public record CreateSubsidyApplicationRequest(
    @NotBlank(message = "applicantName は必須です")
    @Size(max = 100, message = "applicantName は100文字以内で入力してください")
    @Schema(description = "申請者名", example = "山田 太郎")
    String applicantName,
    @NotNull(message = "applicationDate は必須です（例: 2024-06-01）")
    @PastOrPresent(message = "applicationDate は今日以前の日付を指定してください")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "申請日", example = "2024-06-01")
    LocalDate applicationDate,
    @NotNull(message = "amount は必須です（例: 1000000）")
    @DecimalMin(value = "1", message = "amount は1以上を指定してください")
    @Schema(description = "申請金額", example = "1000000")
    BigDecimal amount,
    @NotNull(message = "status は必須です（APPLIED / APPROVED / PAID / WITHDRAWN）")
    @Schema(description = "申請ステータス", example = "APPLIED")
    ApplicationStatus status
) {

}

