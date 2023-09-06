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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectApplicantService {

    private final ProjectApplicantMapper projectApplicantMapper;

    @Transactional(readOnly = true)
    public List<ProjectApplicantsSearchResponseDto> getProjectApplicants(Long projectId) {
        if (!projectApplicantMapper.isExistedProject(projectId)) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND);
        }
        return projectApplicantMapper.selectProjectApplicants(projectId).stream()
                .map(ProjectApplicantsSearchResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectApplicantSearchResponseDto getProjectApplicant(ProjectApplicantSelVo projectApplicantSelVo) {
        if (!projectApplicantMapper.isExistedProject(projectApplicantSelVo.getProjectId())) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND);
        }
        return Optional.ofNullable(projectApplicantMapper.selectProjectApplicant(projectApplicantSelVo))
                .map(ProjectApplicantSearchResponseDto::from)
                .orElseThrow(() -> new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND));
    }

    @Transactional
    public void addProjectApplicant(ProjectApplicantAddVo projectApplicantVo) {
        validateProject(projectApplicantVo.getProjectId());
        validateProjectApplicant(projectApplicantVo.getApplicantId());
        ProjectApplicantSelVo projectApplicantSelVo = ProjectApplicantSelVo.from(projectApplicantVo.getProjectId(), projectApplicantVo.getApplicantId());
        ProjectApplicantResultVo result = projectApplicantMapper.selectProjectApplicant(projectApplicantSelVo);
        if (result != null) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_INPUT_PROJECT_APPLICANT);
        }
        projectApplicantMapper.insertProjectApplicant(projectApplicantVo);
    }

    @Transactional
    public void confirmProjectApplicant(long projectId, long applicantId) {
        validateProject(projectId);
        validateProjectApplicant(applicantId);
        ProjectApplicantSelVo projectApplicantSelVo = ProjectApplicantSelVo.from(projectId, applicantId);
        ProjectApplicantResultVo result = Optional.ofNullable(projectApplicantMapper.selectProjectApplicant(projectApplicantSelVo))
                .orElseThrow(() -> new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND));
        if (result.getStatus().equals(ProjectApplicantStatus.CONFIRMATION)) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_ALREADY_CONFIRM);
        }
        projectApplicantMapper.updateProjectApplicant(
                ProjectApplicantModVo.from(
                        projectId,
                        applicantId,
                        ProjectApplicantStatus.CONFIRMATION));
    }

    private void validateProject(long projectId) {
        if (!projectApplicantMapper.isExistedProject(projectId)) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND);
        }
    }

    private void validateProjectApplicant(long applicantId) {
        if (!projectApplicantMapper.isExistedApplicant(applicantId)) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND);
        }
    }

}
