package com.auction.flab.application.service;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import com.auction.flab.application.mapper.ProjectApplicantMapper;
import com.auction.flab.application.mapper.ProjectApplicantStatus;
import com.auction.flab.application.vo.ProjectApplicantVo;
import com.auction.flab.application.web.dto.ProjectApplicantSearchResponseDto;
import com.auction.flab.application.web.dto.ProjectApplicantsSearchResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProjectApplicantServiceTest {

    @Mock
    ProjectApplicantMapper projectApplicantMapper;
    @InjectMocks
    ProjectApplicantService projectApplicantService;

    @DisplayName("프로젝트 지원자 목록 조회 성공")
    @Test
    void search_project_applicant_list_success() {
        // given
        long projectId = 1L;
        ProjectApplicantVo projectApplicantVo = new ProjectApplicantVo();
        projectApplicantVo.setProjectId(projectId);
        List<ProjectApplicantVo> project = new ArrayList<>();
        project.add(projectApplicantVo);
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicants(eq(1L))).willReturn(project);

        // when
        List<ProjectApplicantsSearchResponseDto> result = projectApplicantService.getProjectApplicants(projectId);

        // then
        assertEquals(projectId, result.get(0).getProjectId());
        then(projectApplicantMapper).should(times(1)).selectProjectApplicants(eq(projectId));
    }

    @DisplayName("프로젝트 지원자 목록 조회 실패 - 프로젝트 미존재")
    @Test
    void search_project_applicant_list_failed_due_to_no_project() {
        // given
        long projectId = 1L;
        ProjectApplicantVo projectApplicantVo = new ProjectApplicantVo();
        projectApplicantVo.setProjectId(projectId);
        List<ProjectApplicantVo> projects = new ArrayList<>();
        projects.add(projectApplicantVo);
        given(projectApplicantMapper.isExistedProject(eq(projectId))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.getProjectApplicants(projectId));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(0)).selectProjectApplicants(eq(projectId));
    }

    @DisplayName("프로젝트 세부 조회 성공")
    @Test
    void search_project_applicant_detail_success() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.from(projectId, applicantId);
        given(projectApplicantMapper.isExistedProject(eq(projectId))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(eq(projectApplicantVo))).willReturn(projectApplicantVo);

        // when
        ProjectApplicantSearchResponseDto projectApplicantSearchResponseDto = projectApplicantService.getProjectApplicant(projectApplicantVo);

        // then
        assertEquals(projectId, projectApplicantSearchResponseDto.getProjectId());
        then(projectApplicantMapper).should(times(1)).selectProjectApplicant(eq(projectApplicantVo));
    }

    @DisplayName("프로젝트 세부 조회 실패 - 프로젝트 미존재")
    @Test
    void search_project_applicant_detail_failed_due_to_no_project() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.from(projectId, applicantId);
        given(projectApplicantMapper.isExistedProject(eq(projectId))).willReturn(false);

        // when
        ProjectException projectException =
                assertThrows(ProjectException.class, () -> projectApplicantService.getProjectApplicant(projectApplicantVo));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(0)).selectProjectApplicant(projectApplicantVo);
    }

    @DisplayName("프로젝트 세부 조회 실패 - 프로젝트 지원자 미존재")
    @Test
    void search_project_applicant_detail_failed_due_to_no_project_applicant() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.from(projectId, applicantId);
        given(projectApplicantMapper.isExistedProject(eq(projectId))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(eq(projectApplicantVo))).willReturn(null);

        // when
        ProjectException projectException =
                assertThrows(ProjectException.class, () -> projectApplicantService.getProjectApplicant(projectApplicantVo));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(1)).selectProjectApplicant(projectApplicantVo);
    }

    @DisplayName("프로젝트 지원 성공")
    @Test
    void applying_project_success() {
        // given
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantVo.class)))
                .willReturn(null);

        // when
        projectApplicantService.addProjectApplicant(projectApplicantVo);

        // then
        then(projectApplicantMapper).should(times(1)).insertProjectApplicant(eq(projectApplicantVo));
    }

    @DisplayName("프로젝트 지원 실패 - 유효하지 않은 프로젝트 ID")
    @Test
    void applying_project_fail_due_to_invalid_project_id() {
        // given
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.addProjectApplicant(projectApplicantVo));

        // then
        then(projectApplicantMapper).should(times(0)).insertProjectApplicant(eq(projectApplicantVo));
    }

    @DisplayName("프로젝트 지원 실패 - 유효하지 않은 지원자 ID")
    @Test
    void applying_project_fail_due_to_invalid_applicant_id() {
        // given
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.addProjectApplicant(projectApplicantVo));

        // then
        then(projectApplicantMapper).should(times(0)).insertProjectApplicant(eq(projectApplicantVo));
    }

    @DisplayName("프로젝트 지원 실패 - 지원 내역 존재")
    @Test
    void applying_project_fail_due_to_duplication() {
        // given
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantVo.class)))
                .willReturn(projectApplicantVo);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.addProjectApplicant(projectApplicantVo));

        // then
        then(projectApplicantMapper).should(times(0)).insertProjectApplicant(eq(projectApplicantVo));
    }

    @DisplayName("프로젝트 지원자 확정 성공")
    @Test
    void confirm_project_applicant_success() {
        // given
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .status(ProjectApplicantStatus.APPLYING)
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantVo.class)))
                .willReturn(projectApplicantVo);

        // when
        projectApplicantService.confirmProjectApplicant(projectApplicantVo);

        // then
        then(projectApplicantMapper).should(times(1)).updateProjectApplicant(eq(projectApplicantVo));
    }

    @DisplayName("프로젝트 지원자 확정 실패 - 유효하지 않은 프로젝트 ID")
    @Test
    void confirm_project_applicant_fail_due_to_invalid_project_id() {
        // given
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .status(ProjectApplicantStatus.APPLYING)
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.confirmProjectApplicant(projectApplicantVo));

        // then
        then(projectApplicantMapper).should(times(0)).updateProjectApplicant(eq(projectApplicantVo));
    }

    @DisplayName("프로젝트 지원자 확정 실패 - 유효하지 않은 지원자 ID")
    @Test
    void confirm_project_applicant_fail_due_to_invalid_applicant_id() {
        // given
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .status(ProjectApplicantStatus.APPLYING)
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.confirmProjectApplicant(projectApplicantVo));

        // then
        then(projectApplicantMapper).should(times(0)).updateProjectApplicant(eq(projectApplicantVo));
    }

    @DisplayName("프로젝트 지원자 확정 실패 - 지원 내역 없음")
    @Test
    void confirm_project_applicant_fail_due_to_no_applying_history() {
        // given
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .status(ProjectApplicantStatus.APPLYING)
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantVo.class)))
                .willReturn(null);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.confirmProjectApplicant(projectApplicantVo));

        // then
        then(projectApplicantMapper).should(times(0)).updateProjectApplicant(eq(projectApplicantVo));
    }

    @DisplayName("프로젝트 지원자 확정 실패 - 이미 확정된 상태")
    @Test
    void confirm_project_applicant_fail_beacuase_already_confirmed() {
        // given
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .status(ProjectApplicantStatus.CONFIRMATION)
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantVo.class)))
                .willReturn(projectApplicantVo);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.confirmProjectApplicant(projectApplicantVo));

        // then
        then(projectApplicantMapper).should(times(0)).updateProjectApplicant(eq(projectApplicantVo));
    }
    
}