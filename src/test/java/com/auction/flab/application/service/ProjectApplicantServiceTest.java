package com.auction.flab.application.service;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import com.auction.flab.application.mapper.ProjectApplicantMapper;
import com.auction.flab.application.mapper.ProjectApplicantStatus;
import com.auction.flab.application.vo.ProjectApplicantAddVo;
import com.auction.flab.application.vo.ProjectApplicantModVo;
import com.auction.flab.application.vo.ProjectApplicantResultVo;
import com.auction.flab.application.vo.ProjectApplicantSelVo;
import com.auction.flab.application.web.dto.ProjectApplicantSearchResponseDto;
import com.auction.flab.application.web.dto.ProjectApplicantsSearchResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        long applicantId = 1L;
        ProjectApplicantResultVo projectApplicantResultVo = ProjectApplicantResultVo.from(projectId, applicantId);
        List<ProjectApplicantResultVo> project = List.of(projectApplicantResultVo);
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
        ProjectApplicantResultVo projectApplicantResultVo = ProjectApplicantResultVo.from(projectId, applicantId);
        ProjectApplicantSelVo projectApplicantSelVo = ProjectApplicantSelVo.from(projectId, applicantId);
        given(projectApplicantMapper.isExistedProject(eq(projectId))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(eq(projectApplicantSelVo))).willReturn(projectApplicantResultVo);

        // when
        ProjectApplicantSearchResponseDto projectApplicantSearchResponseDto = projectApplicantService.getProjectApplicant(projectApplicantSelVo);

        // then
        assertEquals(projectId, projectApplicantSearchResponseDto.getProjectId());
        then(projectApplicantMapper).should(times(1)).selectProjectApplicant(eq(projectApplicantSelVo));
    }

    @DisplayName("프로젝트 세부 조회 실패 - 프로젝트 미존재")
    @Test
    void search_project_applicant_detail_failed_due_to_no_project() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantSelVo projectApplicantSelVo = ProjectApplicantSelVo.from(projectId, applicantId);
        given(projectApplicantMapper.isExistedProject(eq(projectId))).willReturn(false);

        // when
        ProjectException projectException =
                assertThrows(ProjectException.class, () -> projectApplicantService.getProjectApplicant(projectApplicantSelVo));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(0)).selectProjectApplicant(projectApplicantSelVo);
    }

    @DisplayName("프로젝트 세부 조회 실패 - 프로젝트 지원자 미존재")
    @Test
    void search_project_applicant_detail_failed_due_to_no_project_applicant() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantSelVo projectApplicantSelVo = ProjectApplicantSelVo.from(projectId, applicantId);
        given(projectApplicantMapper.isExistedProject(eq(projectId))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(eq(projectApplicantSelVo))).willReturn(null);

        // when
        ProjectException projectException =
                assertThrows(ProjectException.class, () -> projectApplicantService.getProjectApplicant(projectApplicantSelVo));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(1)).selectProjectApplicant(projectApplicantSelVo);
    }

    @DisplayName("프로젝트 지원 성공")
    @Test
    void applying_project_success() {
        // given
        ProjectApplicantAddVo projectApplicantAddVo = ProjectApplicantAddVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantSelVo.class)))
                .willReturn(null);

        // when
        projectApplicantService.addProjectApplicant(projectApplicantAddVo);

        // then
        then(projectApplicantMapper).should(times(1)).insertProjectApplicant(eq(projectApplicantAddVo));
    }

    @DisplayName("프로젝트 지원 실패 - 유효하지 않은 프로젝트 ID")
    @Test
    void applying_project_fail_due_to_invalid_project_id() {
        // given
        ProjectApplicantAddVo projectApplicantAddVo = ProjectApplicantAddVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.addProjectApplicant(projectApplicantAddVo));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(0)).insertProjectApplicant(eq(projectApplicantAddVo));
    }

    @DisplayName("프로젝트 지원 실패 - 유효하지 않은 지원자 ID")
    @Test
    void applying_project_fail_due_to_invalid_applicant_id() {
        // given
        ProjectApplicantAddVo projectApplicantAddVo = ProjectApplicantAddVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.addProjectApplicant(projectApplicantAddVo));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(0)).insertProjectApplicant(eq(projectApplicantAddVo));
    }

    @DisplayName("프로젝트 지원 실패 - 지원 내역 존재")
    @Test
    void applying_project_fail_due_to_duplication() {
        // given
        ProjectApplicantAddVo projectApplicantAddVo = ProjectApplicantAddVo.builder()
                .projectId(1L)
                .applicantId(1L)
                .amount(3_100)
                .period(110)
                .content("이러 저러하게 작업할 예정입니다.")
                .build();
        ProjectApplicantResultVo projectApplicantResultVo = ProjectApplicantResultVo.from(1L, 1L);
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantSelVo.class)))
                .willReturn(projectApplicantResultVo);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.addProjectApplicant(projectApplicantAddVo));
        assertEquals(ErrorCode.EXCEPTION_ON_INPUT_PROJECT_APPLICANT, projectException.getErrorCode());

        // then
        then(projectApplicantMapper).should(times(0)).insertProjectApplicant(eq(projectApplicantAddVo));
    }

    @DisplayName("프로젝트 지원자 확정 성공")
    @Test
    void confirm_project_applicant_success() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantModVo projectApplicantModVo = ProjectApplicantModVo.from(projectId, applicantId, ProjectApplicantStatus.CONFIRMATION);
        ProjectApplicantResultVo projectApplicantResultVo = ProjectApplicantResultVo.from(1L, 1L, ProjectApplicantStatus.APPLYING);
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantSelVo.class)))
                .willReturn(projectApplicantResultVo);

        // when
        projectApplicantService.confirmProjectApplicant(projectId, applicantId);

        // then
        then(projectApplicantMapper).should(times(1)).updateProjectApplicant(eq(projectApplicantModVo));
    }

    @DisplayName("프로젝트 지원자 확정 실패 - 유효하지 않은 프로젝트 ID")
    @Test
    void confirm_project_applicant_fail_due_to_invalid_project_id() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantModVo projectApplicantModVo = ProjectApplicantModVo.from(projectId, applicantId, ProjectApplicantStatus.CONFIRMATION);
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.confirmProjectApplicant(projectId, applicantId));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(0)).updateProjectApplicant(eq(projectApplicantModVo));
    }

    @DisplayName("프로젝트 지원자 확정 실패 - 유효하지 않은 지원자 ID")
    @Test
    void confirm_project_applicant_fail_due_to_invalid_applicant_id() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantModVo projectApplicantModVo = ProjectApplicantModVo.from(projectId, applicantId, ProjectApplicantStatus.CONFIRMATION);
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.confirmProjectApplicant(projectId, applicantId));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(0)).updateProjectApplicant(eq(projectApplicantModVo));
    }

    @DisplayName("프로젝트 지원자 확정 실패 - 지원 내역 없음")
    @Test
    void confirm_project_applicant_fail_due_to_no_applying_history() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantModVo projectApplicantModVo = ProjectApplicantModVo.from(projectId, applicantId, ProjectApplicantStatus.CONFIRMATION);
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantSelVo.class)))
                .willReturn(null);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.confirmProjectApplicant(projectId, applicantId));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(0)).updateProjectApplicant(eq(projectApplicantModVo));
    }

    @DisplayName("프로젝트 지원자 확정 실패 - 이미 확정된 상태")
    @Test
    void confirm_project_applicant_fail_beacuase_already_confirmed() {
        // given
        long projectId = 1L;
        long applicantId = 1L;
        ProjectApplicantModVo projectApplicantModVo = ProjectApplicantModVo.from(projectId, applicantId, ProjectApplicantStatus.CONFIRMATION);
        ProjectApplicantResultVo projectApplicantResultVo = ProjectApplicantResultVo.from(1L, 1L, ProjectApplicantStatus.CONFIRMATION);
        given(projectApplicantMapper.isExistedProject(eq(1L))).willReturn(true);
        given(projectApplicantMapper.isExistedApplicant(eq(1L))).willReturn(true);
        given(projectApplicantMapper.selectProjectApplicant(any(ProjectApplicantSelVo.class)))
                .willReturn(projectApplicantResultVo);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> projectApplicantService.confirmProjectApplicant(projectId, applicantId));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_ALREADY_CONFIRM, projectException.getErrorCode());
        then(projectApplicantMapper).should(times(0)).updateProjectApplicant(eq(projectApplicantModVo));
    }
    
}