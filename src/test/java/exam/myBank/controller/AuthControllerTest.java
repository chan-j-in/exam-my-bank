package exam.myBank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exam.myBank.dto.JoinRequestDto;
import exam.myBank.dto.LoginRequestDto;
import exam.myBank.exception.AppException;
import exam.myBank.exception.ErrorCode;
import exam.myBank.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join() throws Exception {
        // given
        String username = "kim1";
        String password = "1234";

        // when & then
        mockMvc.perform(post("/api/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new JoinRequestDto(username, password, password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - username 중복")
    @WithMockUser
    void join_fail() throws Exception {
        // given
        String username = "kim2";
        String password = "1234";

        // when & then
        when(authService.join(any(JoinRequestDto.class)))
                .thenThrow(new AppException(ErrorCode.USERNAME_DUPLICATED, username + "이(가) 중복됩니다"));

        mockMvc.perform(post("/api/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new JoinRequestDto(username, password, password))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception {
        // given
        String username = "kim3";
        String password = "1234";

        // when & then
        when(authService.login(any(LoginRequestDto.class)))
                .thenReturn("token");

        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(username,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    @WithMockUser
    void login_fail_user_not_found() throws Exception {
        // given
        String username = "nonExistentUser";  // 존재하지 않는 사용자
        String password = "anyPassword";  // 비밀번호는 상관 없음

        // when
        when(authService.login(any(LoginRequestDto.class)))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, username + "을(를) 찾을 수 없습니다."));

        // then
        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(username, password))))
                .andDo(print())
                .andExpect(status().isNotFound());  // 404 응답 예상
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    @WithMockUser
    void login_fail_invalid_password() throws Exception {
        // given
        String username = "kim5";  // 존재하는 사용자
        String correctPassword = "correctPassword";  // 올바른 비밀번호
        String wrongPassword = "wrongPassword";  // 잘못된 비밀번호

        // when
        when(authService.login(any(LoginRequestDto.class)))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호를 확인해주세요."));

        // then
        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(username, wrongPassword))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}