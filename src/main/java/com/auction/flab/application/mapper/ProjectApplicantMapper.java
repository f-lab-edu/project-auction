package com.auction.flab.application.mapper;

import com.auction.flab.application.vo.ProjectApplicantVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectApplicantMapper {

    boolean isExistedProject(Long id);

    List<ProjectApplicantVo> selectProjectApplicants(Long id);

    ProjectApplicantVo selectProjectApplicant(ProjectApplicantVo projectApplicantVo);

}
