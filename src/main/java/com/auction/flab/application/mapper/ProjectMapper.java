package com.auction.flab.application.mapper;

import com.auction.flab.application.vo.PageVo;
import com.auction.flab.application.vo.ProjectVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper {

    List<ProjectVo> selectProjects(PageVo pageVo);

    int selectTotalProjectCount();

    ProjectVo selectProject(Long id);

    int insertProject(ProjectVo projectVo);

    int updateProject(ProjectVo projectVo);

    int deleteProject(Long id);

}
