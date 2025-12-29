package org.example.subsidyapi.staff;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;
import org.example.subsidyapi.controller.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = StaffUserController.class)
@Import(ApiExceptionHandler.class) // 400/409 の JSON を返すハンドラを読み込む
class StaffUserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StaffUserService staffUserService;

  @Test
  void getStaffUsers_roleIsInvalid_returns400() throws Exception {
    mockMvc.perform(get("/staff-users").param("role", "HOGE"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.detail").value("InvalidRequestParameterException"));
  }

  @Test
  void getStaffUserById_notFound_returns404() throws Exception {
    when(staffUserService.getStaffUserById(999L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/staff-users/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createStaffUser_nameIsBlank_returns400() throws Exception {
    // name が空 → 400
    String body = """
        {"name":"","email":"x@example.com","role":"STAFF"}
        """;

    mockMvc.perform(post("/staff-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("name は必須です"));
  }

  @Test
  void createStaffUser_roleIsInvalid_returns400() throws Exception {
    // role が不正 → 400
    String body = """
        {"name":"テスト","email":"x@example.com","role":"HOGE"}
        """;

    mockMvc.perform(post("/staff-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.detail").value("InvalidRequestParameterException"));
  }

  @Test
  void createStaffUser_emailAlreadyExists_returns409() throws Exception {
    // Service が EmailAlreadyExistsException を投げる想定 → 409
    when(staffUserService.createStaffUser(any(), any(), any()))
        .thenThrow(
            new EmailAlreadyExistsException("email はすでに登録されています: dup@example.com"));

    String body = """
        {"name":"重複テスト","email":"dup@example.com","role":"STAFF"}
        """;

    mockMvc.perform(post("/staff-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.error").value("CONFLICT"))
        .andExpect(jsonPath("$.detail").value("EmailAlreadyExistsException"));
  }

  @Test
  void createStaffUser_success_returns201() throws Exception {
    StaffUser created = new StaffUser(
        10L,
        "職員 太郎",
        "ok@example.com",
        StaffRole.STAFF,
        LocalDateTime.now(),
        null
    );

    when(staffUserService.createStaffUser("職員 太郎", "ok@example.com", StaffRole.STAFF))
        .thenReturn(created);

    String body = """
        {"name":"職員 太郎","email":"ok@example.com","role":"STAFF"}
        """;

    mockMvc.perform(post("/staff-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.email").value("ok@example.com"))
        .andExpect(jsonPath("$.role").value("STAFF"));
  }

  @Test
  void updateStaffUser_success_returns200() throws Exception {
    StaffUser updated = new StaffUser(
        1L, "更新 太郎", "updated@example.com", StaffRole.STAFF, LocalDateTime.now(),
        LocalDateTime.now()
    );

    when(staffUserService.updateStaffUser(1L, "更新 太郎", "updated@example.com", StaffRole.STAFF))
        .thenReturn(Optional.of(updated));

    String body = """
        {"name":"更新 太郎","email":"updated@example.com","role":"STAFF"}
        """;

    mockMvc.perform(put("/staff-users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.email").value("updated@example.com"))
        .andExpect(jsonPath("$.role").value("STAFF"));
  }
}
