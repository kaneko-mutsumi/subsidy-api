package org.example.subsidyapi.controller;

import java.math.BigDecimal;
import java.util.List;
import org.example.subsidyapi.service.SubsidyApplicationService;
import org.example.subsidyapi.subsidy.ApplicationStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubsidyApplicationSummaryController {

  private final SubsidyApplicationService service;

  public SubsidyApplicationSummaryController(
      SubsidyApplicationService service) {
    this.service = service;
  }

  @GetMapping("/applications/summary/total-amount")
  public TotalAmountResponse getTotalAmount(
      @RequestParam(required = false) String status
  ) {
    BigDecimal total;
    if (status == null) {
      total = service.getTotalAmount();
    } else {
      ApplicationStatus st = ApplicationStatusParser.parse(status);
      total = service.getTotalAmountByStatus(st);
    }
    return new TotalAmountResponse(total);
  }

  /**
   * ステータスごとの申請額合計を JSON 配列で返します。
   *
   * @return status と totalAmount を持つ集計結果のリスト
   */
  @GetMapping("/applications/summary/total-amount-by-status")
  public List<TotalAmountByStatusResponse> getTotalAmountByStatusList() {
    return service.getTotalAmountByStatusList();
  }
}
