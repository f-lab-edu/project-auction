package com.auction.flab.application.web.controller;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import com.auction.flab.application.service.MemberService;
import com.auction.flab.application.vo.MemberUpdateVo;
import com.auction.flab.application.web.dto.MemberUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @DisplayName("회원 정보 업데이트 요청 성공")
    @Test
    void valid_member_update_request() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!1234567update")
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 회원 미존재")
    @Test
    void invalid_member_update_request_due_to_no_member() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!1234567update")
                .mobileNo("01099998888")
                .build();
        MemberUpdateVo memberUpdateVo = MemberUpdateVo.from(memberUpdateRequestDto);
        willThrow(new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND)).given(memberService).modifyMember(eq(1L), eq(memberUpdateVo));

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 이름이 null인 경우")
    @Test
    void invalid_member_update_request_because_name_is_null() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name(null)
                .password("Aa!1234567update")
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 이름이 공백인 경우")
    @Test
    void invalid_member_update_request_because_name_is_empty() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("")
                .password("Aa!1234567update")
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 이름이 빈칸만 존재하는 경우")
    @Test
    void invalid_member_update_request_because_name_is_blank() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("     ")
                .password("Aa!1234567update")
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 이름이 10자를 초과하는 경우")
    @Test
    void invalid_member_update_request_because_length_of_name_is_greater_than_10() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수12345678")
                .password("Aa!1234567update")
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    
    @DisplayName("회원 정보 업데이트 요청 실패 - 비밀번호가 10자 미만인 경우")
    @Test
    void invalid_member_update_request_because_length_of_name_is_less_than_10() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!123456")
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 비밀번호가 20자리 초과인 경우")
    @Test
    void invalid_member_update_request_because_length_of_name_is_greater_than_20() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!123456712345671234")
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 비밀번호에 대문자 미존재")
    @Test
    void invalid_member_update_request_because_of_no_capitalization() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!1234567update".toLowerCase())
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 비밀번호에 소문자 미존재")
    @Test
    void invalid_member_update_request_because_of_no_lower_case_letter() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!1234567update".toUpperCase())
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 비밀번호에 특수문자 미존재")
    @Test
    void invalid_member_update_request_due_to_missing_special_character() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa1234567update")
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 비밀번호에 숫자 미존재")
    @Test
    void invalid_member_update_request_due_to_missing_number() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aaaaaaaaaaaupdate")
                .mobileNo("01099998888")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 모바일 번호가 13자리 초과")
    @Test
    void invalid_member_update_request_because_length_of_mobile_no_is_greater_than_13() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!1234567update")
                .mobileNo("01099998888111")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 업데이트 요청 실패 - 모바일 번호에 숫자 외 문자 존재")
    @Test
    void invalid_member_update_request_due_to_presence_of_non_numeric_characters_in_mobile_no() throws Exception {
        // given
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!1234567update")
                .mobileNo("0109999888a")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 정보 삭제 요청 성공")
    @Test
    void valid_member_delete_request() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(delete("/members/1"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원 정보 삭제 요청 실패 - 회원 미존재")
    @Test
    void invalid_member_delete_request_due_to_no_member() throws Exception {
        // given
        willThrow(new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND)).given(memberService).removeMember(eq(1L));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/members/1"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}