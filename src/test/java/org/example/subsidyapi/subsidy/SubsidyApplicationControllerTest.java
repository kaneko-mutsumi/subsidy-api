package org.example.subsidyapi.subsidy;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.subsidyapi.controller.ApiExceptionHandler;
import org.example.subsidyapi.controller.SubsidyApplicationController;
import org.example.subsidyapi.service.SubsidyApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SubsidyApplicationController.class)
@Import(ApiExceptionHandler.class)
class SubsidyApplicationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SubsidyApplicationService subsidyApplicationService;

  @Test
  void createApplication_invalidAmount_returns400() throws Exception {
    String body = """
        {
          "applicantName":"申請 太郎",
          "applicationDate":"2024-06-01",
          "amount":"abc",
          "status":"APPLIED"
        }
        """;

    mockMvc.perform(post("/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("amount の形式が不正です: abc"))
        .andExpect(jsonPath("$.detail").value("HttpMessageNotReadableException"));
  }

  @Test
  void createApplication_invalidStatus_returns400() throws Exception {
    String body = """
        {
          "applicantName":"申請 太郎",
          "applicationDate":"2024-06-01",
          "amount":1000000,
          "status":"HOGE"
        }
        """;

    mockMvc.perform(post("/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("status の形式が不正です: HOGE"))
        .andExpect(jsonPath("$.detail").value("HttpMessageNotReadableException"));
  }

  @Test
  void createApplication_success_returns201() throws Exception {
    SubsidyApplication created = new SubsidyApplication(
        1L,
        "申請 太郎",
        LocalDate.of(2024, 6, 1),
        new BigDecimal("1000000"),
        ApplicationStatus.APPLIED
    );

    when(subsidyApplicationService.createApplication(
        eq("申請 太郎"),
        eq(LocalDate.of(2024, 6, 1)),
        eq(new BigDecimal("1000000")),
        eq(ApplicationStatus.APPLIED)))
        .thenReturn(created);

    String body = """
        {
          "applicantName":"申請 太郎",
          "applicationDate":"2024-06-01",
          "amount":1000000,
          "status":"APPLIED"
        }
        """;

    mockMvc.perform(post("/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.applicantName").value("申請 太郎"))
        .andExpect(jsonPath("$.applicationDate").value("2024-06-01"))
        .andExpect(jsonPath("$.amount").value(1000000))
        .andExpect(jsonPath("$.status").value("APPLIED"));
  }
}