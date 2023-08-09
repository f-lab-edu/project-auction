package com.auction.flab.application.service;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.InternalException;
import com.auction.flab.application.mapper.ProjectMapper;
import com.auction.flab.application.mapper.ProjectStatus;
import com.auction.flab.application.vo.PageVo;
import com.auction.flab.application.vo.ProjectVo;
import com.auction.flab.application.web.dto.ProjectRequestDto;
import com.auction.flab.application.web.dto.ProjectResponseDto;
import com.auction.flab.application.web.dto.ProjectSearchResponseDto;
import com.auction.flab.application.web.dto.ProjectsSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectMapper projectMapper;

    @Transactional(readOnly = true)
    public ProjectsSearchResponseDto getProjects(int page, int size) {
        PageVo pageVo = PageVo.from(page, size);
        List<ProjectSearchResponseDto> projects = projectMapper.selectProjects(pageVo).stream()
                .map(ProjectSearchResponseDto::from)
                .toList();
        pageVo.setTotalCount(projectMapper.selectTotalProjectCount());
        return ProjectsSearchResponseDto.from(projects, pageVo);
    }

    @Transactional(readOnly = true)
    public ProjectSearchResponseDto getProject(Long id) {
        ProjectVo projectVo = Optional.ofNullable(projectMapper.selectProject(id))
                .orElseThrow(() -> new InternalException(ErrorCode.EXCEPTION_ON_NOT_FOUND));
        return ProjectSearchResponseDto.from(projectVo);
    }

    @Transactional
    public ProjectResponseDto addProject(ProjectRequestDto projectRequestDto) {
        ProjectVo projectVo = ProjectVo.from(projectRequestDto);
        projectMapper.insertProject(projectVo);
        if (projectVo.getId() == null) {
            throw new InternalException(ErrorCode.EXCEPTION_ON_INPUT_PROJECT);
        }
        return ProjectResponseDto.from(projectVo.getId());
    }

    @Transactional
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto projectRequestDto) {
        ProjectVo result = Optional.ofNullable(projectMapper.selectProject(id))
                .orElseThrow(() -> new InternalException(ErrorCode.EXCEPTION_ON_UPDATE_PROJECT));
        if (result.getStatus().equals(ProjectStatus.CONFIRMATION)) {
            throw new InternalException(ErrorCode.EXCEPTION_ON_UPDATE_PROJECT);
        }
        ProjectVo projectVo = ProjectVo.from(projectRequestDto);
        projectVo.setId(id);
        projectMapper.updateProject(projectVo);
        return ProjectResponseDto.from(id);
    }

    @Transactional
    public void deleteProject(Long id) {
        ProjectVo result = Optional.ofNullable(projectMapper.selectProject(id))
                .orElseThrow(() -> new InternalException(ErrorCode.EXCEPTION_ON_DELETE_PROJECT));
        if (!result.getStatus().equals(ProjectStatus.PROPOSAL)) {
            throw new InternalException(ErrorCode.EXCEPTION_ON_DELETE_PROJECT);
        }
        projectMapper.deleteProject(id);
    }

}
