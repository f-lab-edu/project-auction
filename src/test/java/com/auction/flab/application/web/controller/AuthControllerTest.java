package com.auction.flab.application.web.controller;

import com.auction.flab.application.service.AuthService;
import com.auction.flab.application.util.TokenUtil;
import com.auction.flab.application.vo.AuthVo;
import com.auction.flab.application.web.dto.SignInRequestDto;
import com.auction.flab.application.web.dto.SignUpRequestDto;
import com.auction.flab.application.web.dto.SignUpResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @MockBean
    TokenUtil tokenUtil;

    static AuthVo authVo;

    @BeforeAll
    static void setUp() {
        authVo = AuthVo.from(SignInRequestDto.builder()
                .email("test@gmail.com")
                .password("Aa!1234567")
                .build());
    }

    @Test
    void valid_sign_up_request() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void invalid_name_with_null() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name(null)
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_name_with_empty() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("")
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_name_with_blank() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("     ")
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_name_with_length_greater_than_10() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동12345678")
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_email_with_null() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email(null)
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_email_with_empty() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_email_with_blank() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("     ")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_email_with_length_greater_than_30() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test123456789012345678901234567890@gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_email_with_worng_format() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("tes#gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_password_with_length_is_less_than_10() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!123456")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_password_with_greater_than_20() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!123456712345671234")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_password_due_to_lack_of_capitalization() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!1234567".toLowerCase())
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_password_due_to_lack_of_lower_case_letters() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!1234567".toUpperCase())
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_password_due_to_missing_special_characters() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa12345678")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_password_due_to_missing_number() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!abcdefg")
                .mobileNo("01012345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_mobileNo_length_is_greater_than_13() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("0101234567812345")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalid_mobileNo_due_to_presence_of_non_numeric_characters() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("010-12345678")
                .build();
        given(authService.signUp(eq(signUpRequestDto))).willReturn(SignUpResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void valid_sign_in_request() throws Exception {
        // given
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .email("test@gmail.com")
                .password("Aa!1234567")
                .build();
        given(authService.signIn(eq(signInRequestDto))).willReturn("test_token");

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("test_token"));
    }

}
