package com.auction.flab.application.mapper;

import com.auction.flab.application.vo.ProjectApplicantAddVo;
import com.auction.flab.application.vo.ProjectApplicantModVo;
import com.auction.flab.application.vo.ProjectApplicantResultVo;
import com.auction.flab.application.vo.ProjectApplicantSelVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectApplicantMapper {

    boolean isExistedProject(long projectId);

    boolean isExistedApplicant(long applicantId);

    List<ProjectApplicantResultVo> selectProjectApplicants(long projectId);

    ProjectApplicantResultVo selectProjectApplicant(ProjectApplicantSelVo projectApplicantSelVo);

    int insertProjectApplicant(ProjectApplicantAddVo projectApplicantAddVo);

    int updateProjectApplicant(ProjectApplicantModVo projectApplicantModVo);

}
