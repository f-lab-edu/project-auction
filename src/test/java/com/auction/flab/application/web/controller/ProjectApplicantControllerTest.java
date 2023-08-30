package com.auction.flab.application.web.controller;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import com.auction.flab.application.service.ProjectApplicantService;
import com.auction.flab.application.vo.ProjectApplicantSelVo;
import com.auction.flab.application.web.dto.ProjectApplicantRequestDto;
import com.auction.flab.application.web.dto.ProjectApplicantSearchResponseDto;
import com.auction.flab.application.web.dto.ProjectApplicantsSearchResponseDto;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ProjectApplicantController.class)
class ProjectApplicantControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProjectApplicantService projectApplicantService;

    @DisplayName("지원자 목록 조회 요청 성공")
    @Test
    void valid_search_list_request() throws Exception {
        // given
        Long id = 1L;
        ProjectApplicantsSearchResponseDto projectApplicantsSearchResponseDto = ProjectApplicantsSearchResponseDto.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_000)
                .period(100)
                .email("test@gmail.com")
                .build();
        List<ProjectApplicantsSearchResponseDto> result = List.of(projectApplicantsSearchResponseDto);
        given(projectApplicantService.getProjectApplicants(eq(id))).willReturn(result);

        // when
        ResultActions resultActions = mockMvc.perform(get("/projects/1/applicants"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].project_id").value(1L))
                .andExpect(jsonPath("[0].applicant_id").value(1L))
                .andExpect(jsonPath("[0].amount").value(3_000))
                .andExpect(jsonPath("[0].period").value(100))
                .andExpect(jsonPath("[0].email").value("test@gmail.com"));
    }

    @DisplayName("지원자 목록 조회 실패 - 요청 ID 없음")
    @Test
    void invalid_search_list_request_because_due_to_no_id() throws Exception {
        // given
        given(projectApplicantService.getProjectApplicants(eq(1L)))
                .willThrow(new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND));

        // when
        ResultActions resultActions = mockMvc.perform(get("/projects/1/applicants"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("지원자 세부내용 조회 요청 성공")
    @Test
    void valid_search_detail_request() throws Exception {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        int amount = 3000;
        int period = 100;
        String name = "날씨 정보 오픈 API 프로젝트";
        String email = "test@gmail.com";
        String content = "날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.";
        int orderAmount = 3100;
        int executionPeriod = 110;
        String supportContent = "이러 저러하게 작업할 예정입니다.";
        ProjectApplicantSearchResponseDto projectApplicantSearchResponseDto = ProjectApplicantSearchResponseDto.builder()
                .projectId(projectId)
                .applicantId(applicantId)
                .amount(amount)
                .period(period)
                .name(name)
                .email(email)
                .content(content)
                .orderAmount(orderAmount)
                .executionPeriod(executionPeriod)
                .supportContent(supportContent)
                .build();
        ProjectApplicantSelVo projectApplicantSelVo = ProjectApplicantSelVo.from(projectId, applicantId);
        given(projectApplicantService.getProjectApplicant(eq(projectApplicantSelVo)))
                .willReturn(projectApplicantSearchResponseDto);

        // when
        ResultActions resultActions = mockMvc.perform(get("/projects/1/applicants/1"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.project_id").value(projectId))
                .andExpect(jsonPath("$.applicant_id").value(applicantId))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.period").value(period))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.order_amount").value(orderAmount))
                .andExpect(jsonPath("$.execution_period").value(executionPeriod))
                .andExpect(jsonPath("$.support_content").value(supportContent));
    }

    @DisplayName("지원자 세부내용 조회 요청 실패 - 요청 프로젝트 ID 없음")
    @Test
    void invalid_search_detail_request_due_to_no_project_id() throws Exception {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantSelVo projectApplicantSelVo = ProjectApplicantSelVo.from(projectId, applicantId);
        given(projectApplicantService.getProjectApplicant(eq(projectApplicantSelVo)))
                .willThrow(new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND));

        // when
        ResultActions resultActions = mockMvc.perform(get("/projects/1/applicants/1"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("지원자 세부내용 조회 요청 실패 - 요청 지원자 ID 없음")
    @Test
    void invalid_search_detail_request_due_to_no_applicant_id() throws Exception {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantSelVo projectApplicantSelVo = ProjectApplicantSelVo.from(projectId, applicantId);
        given(projectApplicantService.getProjectApplicant(eq(projectApplicantSelVo)))
                .willThrow(new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND));

        // when
        ResultActions resultActions = mockMvc.perform(get("/projects/1/applicants/1"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("프로젝트 지원 요청 성공")
    @Test
    void valid_insert_request() throws Exception {
        // given
        long projectId = 1L;
        ProjectApplicantRequestDto projectApplicantRequestDto = ProjectApplicantRequestDto.builder()
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/projects/" + projectId + "/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectApplicantRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("프로젝트 지원 요청 실패 - 지원자 ID가 null 인 경우")
    @Test
    void invalid_applicantId_with_null() throws Exception {
        long projectId = 1L;
        ProjectApplicantRequestDto projectApplicantRequestDto = ProjectApplicantRequestDto.builder()
                .applicantId(null)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/projects/" + projectId + "/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectApplicantRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로젝트 지원 요청 실패 - 예상금액이 최대 금액을 넘어서는 경우")
    @Test
    void invalid_amount_above_the_maximum() throws Exception {
        long projectId = 1L;
        ProjectApplicantRequestDto projectApplicantRequestDto = ProjectApplicantRequestDto.builder()
                .applicantId(1L)
                .amount(100_001)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/projects/" + projectId + "/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectApplicantRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로젝트 지원 요청 실패 - 예상기간이 최소기간 미만인 경우")
    @Test
    void invalid_period_below_the_minimum() throws Exception {
        long projectId = 1L;
        ProjectApplicantRequestDto projectApplicantRequestDto = ProjectApplicantRequestDto.builder()
                .applicantId(1L)
                .amount(3_100)
                .period(0)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/projects/" + projectId + "/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectApplicantRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로젝트 지원 요청 실패 - 예상기간이 최대기간 초과인 경우")
    @Test
    void invalid_period_above_the_maximum() throws Exception {
        long projectId = 1L;
        ProjectApplicantRequestDto projectApplicantRequestDto = ProjectApplicantRequestDto.builder()
                .applicantId(1L)
                .amount(3_100)
                .period(1_001)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/projects/" + projectId + "/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectApplicantRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로젝트 지원 요청 실패 - 업무내용이 null인 경우")
    @Test
    void invalid_content_with_null() throws Exception {
        long projectId = 1L;
        ProjectApplicantRequestDto projectApplicantRequestDto = ProjectApplicantRequestDto.builder()
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content(null)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/projects/" + projectId + "/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectApplicantRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로젝트 지원 요청 실패 - 업무내용이 공백인 경우")
    @Test
    void invalid_content_with_empty() throws Exception {
        long projectId = 1L;
        ProjectApplicantRequestDto projectApplicantRequestDto = ProjectApplicantRequestDto.builder()
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/projects/" + projectId + "/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectApplicantRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로젝트 지원 요청 실패 - 업무내용이 빈칸만 존재하는 경우")
    @Test
    void invalid_content_with_blank() throws Exception {
        long projectId = 1L;
        ProjectApplicantRequestDto projectApplicantRequestDto = ProjectApplicantRequestDto.builder()
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("     ")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/projects/" + projectId + "/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectApplicantRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로젝트 지원 요청 실패 - 업무내용이 최대길이 초과인 경우")
    @Test
    void invalid_content_beyond_maximum_length() throws Exception {
        String[] contents = new String[2001];
        Arrays.fill(contents, "a");
        long projectId = 1L;
        ProjectApplicantRequestDto projectApplicantRequestDto = ProjectApplicantRequestDto.builder()
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content(String.join("", contents))
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/projects/" + projectId + "/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectApplicantRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로젝트 지원자 확정 요청 성공")
    @Test
    void valid_project_applicant_confirm_request() throws Exception {
        // given
        long projectId = 1L;
        long applicantId = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(put("/projects/" + projectId + "/applicants/" + applicantId));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

}