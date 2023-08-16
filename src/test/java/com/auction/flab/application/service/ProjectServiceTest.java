package com.auction.flab.application.service;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import com.auction.flab.application.mapper.ProjectMapper;
import com.auction.flab.application.mapper.ProjectStatus;
import com.auction.flab.application.vo.PageVo;
import com.auction.flab.application.vo.ProjectVo;
import com.auction.flab.application.web.dto.ProjectRequestDto;
import com.auction.flab.application.web.dto.ProjectResponseDto;
import com.auction.flab.application.web.dto.ProjectSearchResponseDto;
import com.auction.flab.application.web.dto.ProjectsSearchResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    ProjectMapper projectMapper;
    @InjectMocks
    ProjectService projectService;

    static LocalDateTime deadline;

    static LocalDateTime startDate;

    @BeforeAll
    static void init() {
        deadline = LocalDateTime.now().plusDays(10);
        startDate = deadline.plusDays(3);
    }

    @DisplayName("프로젝트 추가 성공")
    @Test
    void add_project_success() {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        ProjectVo projectVo = ProjectVo.from(projectRequestDto);
        given(projectMapper.insertProject(eq(projectVo))).will(invocation -> {
            projectVo.setId(1L);
            ProjectVo arg = invocation.getArgument(0);
            arg.setId(1L);
            return 1;
        });

        // when
        ProjectResponseDto projectResponseDto = projectService.addProject(projectRequestDto);

        // then
        assertEquals(1L, projectResponseDto.getId());
        then(projectMapper).should(times(1)).insertProject(eq(projectVo));
    }

    @DisplayName("프로젝트 추가 실패 - ID 채번 오류")
    @Test
    void add_project_failed_due_to_db_exception() {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        given(projectMapper.insertProject(eq(ProjectVo.from(projectRequestDto)))).willReturn(1);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectService.addProject(projectRequestDto));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_INPUT_PROJECT, projectException.getErrorCode());
        then(projectMapper).should(times(1)).insertProject(eq(ProjectVo.from(projectRequestDto)));
    }

    @DisplayName("프로젝트 수정 성공")
    @Test
    void update_project_success() {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트(수정)")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        Long id = 1L;
        ProjectVo projectVo = ProjectVo.from(projectRequestDto);
        projectVo.setId(id);
        projectVo.setStatus(ProjectStatus.PROPOSAL);
        given(projectMapper.selectProject(eq(id))).willReturn(projectVo);
        given(projectMapper.updateProject(eq(projectVo))).willReturn(1);

        // when
        ProjectResponseDto projectResponseDto = projectService.updateProject(id, projectRequestDto);

        // then
        assertEquals(id, projectResponseDto.getId());
        then(projectMapper).should(times(1)).updateProject(eq(projectVo));
    }

    @DisplayName("프로젝트 수정 실패 - 정보를 업데이트할 프로젝트가 미존재")
    @Test
    void update_project_failed_due_to_no_project() {
        // given
        Long projectId = 1L;
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        given(projectMapper.selectProject(eq(projectId))).willReturn(null);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectService.updateProject(projectId, projectRequestDto));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_UPDATE_PROJECT, projectException.getErrorCode());
        then(projectMapper).should(times(1)).selectProject(projectId);
    }

    @DisplayName("프로젝트 수정 실패 - 프로젝트가 confirmation 상태인 경우 수정 불가")
    @Test
    void update_project_failed_due_to_invalid_status() {
        // given
        Long projectId = 1L;
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        ProjectVo projectVo = ProjectVo.from(projectRequestDto);
        projectVo.setStatus(ProjectStatus.CONFIRMATION);
        given(projectMapper.selectProject(eq(projectId))).willReturn(projectVo);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectService.updateProject(projectId, projectRequestDto));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_UPDATE_PROJECT, projectException.getErrorCode());
        then(projectMapper).should(times(1)).selectProject(projectId);
    }

    @DisplayName("프로젝트 삭제 성공")
    @Test
    void delete_project_success() {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트(수정)")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        Long projectId = 1L;
        ProjectVo projectVo = ProjectVo.from(projectRequestDto);
        projectVo.setId(projectId);
        projectVo.setStatus(ProjectStatus.PROPOSAL);
        given(projectMapper.selectProject(eq(projectId))).willReturn(projectVo);
        given(projectMapper.deleteProject(eq(projectId))).willReturn(1);

        // when
        projectService.deleteProject(projectId);

        // then
        then(projectMapper).should(times(1)).deleteProject(eq(projectId));
    }

    @DisplayName("프로젝트 삭제 실패 - 삭제할 프로젝트가 미존재")
    @Test
    void delete_project_failed_due_to_no_project() {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        Long projectId = 1L;
        given(projectMapper.selectProject(eq(projectId))).willReturn(null);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectService.deleteProject(projectId));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_DELETE_PROJECT, projectException.getErrorCode());
        then(projectMapper).should(times(1)).selectProject(projectId);
    }

    @DisplayName("프로젝트 삭제 실패 - 프로젝트가 proposal 상태가 아닌 경우 삭제 불가")
    @Test
    void delete_project_failed_due_to_invalid_status() {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .proposerId(1L)
                .name("날씨 정보 오픈 API 프로젝트")
                .amount(3_000)
                .period(100)
                .deadline(deadline)
                .startDate(startDate)
                .content("날짜 정보를 제공하는 API를 작성하는 프로젝트 입니다.")
                .build();
        Long projectId = 1L;
        ProjectVo projectVo = ProjectVo.from(projectRequestDto);
        projectVo.setId(projectId);
        projectVo.setStatus(ProjectStatus.PROCEEDING);
        given(projectMapper.selectProject(eq(projectId))).willReturn(projectVo);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectService.deleteProject(projectId));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_DELETE_PROJECT, projectException.getErrorCode());
        then(projectMapper).should(times(1)).selectProject(projectId);
    }

    @DisplayName("프로젝트 목록 조회 성공")
    @Test
    void search_project_list_success() {
        // given
        int page = 1;
        int size = 10;
        PageVo pageVo = PageVo.from(page, size);
        ProjectVo projectVo = new ProjectVo();
        projectVo.setId(1L);
        List<ProjectVo> projects = new ArrayList<>();
        projects.add(projectVo);
        given(projectMapper.selectProjects(eq(pageVo))).willReturn(projects);

        // when
        ProjectsSearchResponseDto projectsSearchResponseDto = projectService.getProjects(page, size);

        // then
        assertEquals(1L, projectsSearchResponseDto.getData().get(0).getId());
        then(projectMapper).should(times(1)).selectProjects(eq(pageVo));
    }

    @DisplayName("프로젝트 세부 조회 성공")
    @Test
    void search_project_detail_success() {
        // given
        Long projectId = 1L;
        ProjectVo projectVo = new ProjectVo();
        projectVo.setId(projectId);
        given(projectMapper.selectProject(eq(projectId))).willReturn(projectVo);

        // when
        ProjectSearchResponseDto projectSearchResponseDto = projectService.getProject(projectId);

        // then
        assertEquals(projectId, projectSearchResponseDto.getId());
        then(projectMapper).should(times(1)).selectProject(eq(projectId));
    }

    @DisplayName("프로젝트 세부 조회 실패 - 프로젝트 미존재")
    @Test
    void search_project_detail_failed_due_to_no_project() {
        // given
        Long projectId = 111L;
        given(projectMapper.selectProject(eq(projectId))).willReturn(null);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectService.getProject(projectId));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectMapper).should(times(1)).selectProject(projectId);
    }

}