package com.auction.flab.application.web.controller;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.InternalException;
import com.auction.flab.application.service.ProjectService;
import com.auction.flab.application.web.dto.ProjectRequestDto;
import com.auction.flab.application.web.dto.ProjectResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ProjectController.class)
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProjectService projectService;

    static LocalDateTime deadline;

    static LocalDateTime startDate;

    @BeforeAll
    static void init() {
        deadline = LocalDateTime.now().plusDays(10);
        startDate = deadline.plusDays(3);
    }

    @DisplayName("추가 요청 성공")
    @Test
    void valid_insert_request() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("요청 실패 - 제안자 ID가 null 인 경우")
    @Test
    void invalid_proposerId_with_null() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(null)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 프로젝트명이 null 인 경우")
    @Test
    void invalid_name_with_null() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name(null)
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 프로젝트명이 없는 경우")
    @Test
    void invalid_name_with_empty() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 프로젝트명이 빈칸만 존재하는 경우")
    @Test
    void invalid_name_with_blank() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("     ")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 예상금액이 null 인 경우")
    @Test
    void invalid_amount_with_null() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(null)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 예상금액이 최소 금액에 못미치는 경우")
    @Test
    void invalid_amount_below_the_minimum() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(0)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 예상금액이 최대 금액을 넘어서는 경우")
    @Test
    void invalid_amount_above_the_maximum() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(100_001)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 예상기간이 null인 경우")
    @Test
    void invalid_period_with_null() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(null)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 예상기간이 최소기간 미만인 경우")
    @Test
    void invalid_period_below_the_minimum() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(0)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 예상기간이 최대기간 초과인 경우")
    @Test
    void invalid_period_above_the_maximum() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(1_001)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 모집마감일이 null인 경우")
    @Test
    void invalid_deadline_with_null() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(null)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 모집마감일이 과거인 경우")
    @Test
    void invalid_deadline_with_past_date() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(LocalDateTime.now().minusSeconds(1L))
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 예상시작일이 null인 경우")
    @Test
    void invalid_startDate_with_null() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(null)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 예상시작일이 과거인 경우")
    @Test
    void invalid_startDate_with_past_date() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(LocalDateTime.now().minusSeconds(1L))
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 업무내용이 null인 경우")
    @Test
    void invalid_content_with_null() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content(null)
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 업무내용이 공백인 경우")
    @Test
    void invalid_content_with_empty() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("")
                .build();
        projectRequestDto.setContent("");

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 업무내용이 빈칸만 존재하는 경우")
    @Test
    void invalid_content_with_blank() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("     ")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 업무내용이 최대길이 초과인 경우")
    @Test
    void invalid_content_beyond_maximum_length() throws Exception {
        String[] contents = new String[2001];
        Arrays.fill(contents, "a");
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content(String.join("", contents))
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 실패 - 예상시작일이 모집마감일보다 과거인 경우")
    @Test
    void invalid_request_because_startDate_is_less_than_deadline() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(startDate.plusDays(1L))
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();

        given(projectService.addProject(eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("수정 요청 성공")
    @Test
    void valid_update_request() throws Exception {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트(수정)")
                .amount(3_000)
                .period(100)
                .deadline(LocalDateTime.of(2023, 9, 3, 0, 0, 0))
                .startDate(LocalDateTime.of(2023, 9, 11, 0, 0, 0))
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        Long id = 1L;
        given(projectService.updateProject(eq(id), eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(put("/projects/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("수정 요청 실패 - 요청 ID 없음")
    @Test
    void invalid_update_request_due_to_no_id() throws Exception {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트(수정)")
                .amount(3_000)
                .period(100)
                .deadline(LocalDateTime.of(2023, 9, 3, 0, 0, 0))
                .startDate(LocalDateTime.of(2023, 9, 11, 0, 0, 0))
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        Long id = 1L;
        given(projectService.updateProject(eq(id), eq(projectRequestDto))).willReturn(ProjectResponseDto.from(1L));

        // when
        ResultActions resultActions = mockMvc.perform(put("/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("삭제 요청 성공")
    @Test
    void valid_delete_request() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(delete("/projects/1"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("삭제 요청 실패 - 요청 ID 없음")
    @Test
    void invalid_delete_request_due_to_no_id() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(delete("/projects/"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("목록 조회 요청 성공")
    @Test
    void valid_search_list_request() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/projects?page=1&size=10"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("목록 조회 실패 - page가 1보다 작음")
    @Test
    void invvalid_search_list_request_because_page_is_less_than_1() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/projects?page=0&size=10"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("목록 조회 실패 - size가 1보다 작음")
    @Test
    void invvalid_search_list_request_because_size_is_less_than_1() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/projects?page=10&size=0"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("세부내용 조회 요청 성공")
    @Test
    void valid_search_detail_request() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/projects/1"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("세부내용 조회 요청 실패 - 요청 ID 없음")
    @Test
    void invalid_search_detail_request_due_to_no_id() throws Exception {
        // given
        Long id = 33333L;
        given(projectService.getProject(eq(id))).willThrow(new InternalException(ErrorCode.EXCEPTION_ON_NOT_FOUND));

        // when
        ResultActions resultActions = mockMvc.perform(get("/projects/" + id));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    
}