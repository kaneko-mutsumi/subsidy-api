package org.example.subsidyapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
  private SubsidyApplicationService service;

  @Test
  void createApplication_applicationDateIsInvalid_returns400() throws Exception {
    String body = """
        {"applicantName":"申請者","applicationDate":"2024/01/01","amount":"1000","status":"APPLIED"}
        """;

    mockMvc.perform(post("/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("applicationDate の形式が不正です（例: 2024-06-01）"))
        .andExpect(jsonPath("$.detail").value("MethodArgumentNotValidException"))
        .andExpect(jsonPath("$.errors[0].field").value("applicationDate"))
        .andExpect(jsonPath("$.errors[0].message").value("applicationDate の形式が不正です（例: 2024-06-01）"));
  }

  @Test
  void createApplication_amountIsInvalid_returns400() throws Exception {
    String body = """
        {"applicantName":"申請者","applicationDate":"2024-01-01","amount":"1,000","status":"APPLIED"}
        """;

    mockMvc.perform(post("/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("amount の形式が不正です（例: 1000000）"))
        .andExpect(jsonPath("$.detail").value("MethodArgumentNotValidException"))
        .andExpect(jsonPath("$.errors[0].field").value("amount"))
        .andExpect(jsonPath("$.errors[0].message").value("amount の形式が不正です（例: 1000000）"));
  }
}
