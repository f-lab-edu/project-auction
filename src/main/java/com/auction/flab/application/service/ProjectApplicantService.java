package com.auction.flab.application.service;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import com.auction.flab.application.mapper.ProjectApplicantMapper;
import com.auction.flab.application.vo.ProjectApplicantVo;
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
    public List<ProjectApplicantsSearchResponseDto> getProjectApplicants(Long id) {
        if (!projectApplicantMapper.isExistedProject(id)) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND);
        }
        return projectApplicantMapper.selectProjectApplicants(id).stream()
                .map(ProjectApplicantsSearchResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectApplicantSearchResponseDto getProjectApplicant(ProjectApplicantVo projectApplicantVo) {
        if (!projectApplicantMapper.isExistedProject(projectApplicantVo.getProjectId())) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND);
        }
        return Optional.ofNullable(projectApplicantMapper.selectProjectApplicant(projectApplicantVo))
                .map(ProjectApplicantSearchResponseDto::from)
                .orElseThrow(() -> new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND));
    }

}