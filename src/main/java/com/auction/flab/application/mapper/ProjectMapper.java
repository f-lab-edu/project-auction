package com.auction.flab.application.mapper;

import com.auction.flab.application.vo.ProjectVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper {

    ProjectVo selectProject(Long id);

    int insertProject(ProjectVo projectVo);

    int updateProject(ProjectVo projectVo);

    int deleteProject(Long id);

}
