package com.auction.flab.application.mapper;

import com.auction.flab.application.vo.ProjectApplicantVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectApplicantMapper {

    boolean isExistedProject(Long projectId);

    boolean isExistedApplicant(Long applicantId);

    List<ProjectApplicantVo> selectProjectApplicants(Long id);

    ProjectApplicantVo selectProjectApplicant(ProjectApplicantVo projectApplicantVo);

    int insertProjectApplicant(ProjectApplicantVo projectApplicantVo);

    int updateProjectApplicant(ProjectApplicantVo projectApplicantVo);

}
