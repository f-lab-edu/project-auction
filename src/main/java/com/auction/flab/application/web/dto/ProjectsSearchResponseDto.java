package com.auction.flab.application.web.dto;

import com.auction.flab.application.vo.PageVo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProjectsSearchResponseDto {

    private List<ProjectSearchResponseDto> data;
    private PageVo metaData;

    public static ProjectsSearchResponseDto from(List<ProjectSearchResponseDto> projects, PageVo pageVo) {
        return ProjectsSearchResponseDto.builder().data(projects).metaData(pageVo).build();
    }

}
