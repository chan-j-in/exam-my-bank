package exam.myBank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exam.myBank.domain.dto.memberDto.JoinRequestDto;
import exam.myBank.domain.dto.memberDto.LoginRequestDto;
import exam.myBank.exception.AppException;
import exam.myBank.exception.ErrorCode;
import exam.myBank.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

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
        mockMvc.perform(post("/api/members/join")
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
        when(memberService.join(any(), any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_DUPLICATED, username + "이(가) 중복됩니다"));

        mockMvc.perform(post("/api/members/join")
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
        when(memberService.login(any(), any()))
                .thenReturn("token");

        mockMvc.perform(post("/api/members/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(username,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    @WithMockUser
    void login_fail() throws Exception {
        // given
        String username = "kim4";
        String password = "1234";

        // when & then
        when(memberService.join(any(), any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, username + "을(를) 찾을 수 없습니다."));

        mockMvc.perform(post("/api/members/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new JoinRequestDto(username, password, password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호")
    @WithMockUser
    void login_fail2() throws Exception {
        // given
        String username = "kim5";
        String password = "1234";

        // when & then
        when(memberService.join(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호를 확인해주세요."));

        mockMvc.perform(post("/api/members/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new JoinRequestDto(username, password, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}