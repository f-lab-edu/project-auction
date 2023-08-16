package com.auction.flab.application.web.dto;

import com.auction.flab.application.vo.ProjectVo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSearchResponseDto {

    private Long id;
    private Long proposerId;
    private String name;
    private int amount;
    private int period;
    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private String content;
    private Integer applicants;
    private LocalDateTime createdDate;

    public static ProjectSearchResponseDto from(ProjectVo projectVo) {
        return ProjectSearchResponseDto.builder()
                .id(projectVo.getId())
                .proposerId(projectVo.getProposerId())
                .name(projectVo.getName())
                .amount(projectVo.getAmount())
                .period(projectVo.getPeriod())
                .deadline(projectVo.getDeadline())
                .startDate(projectVo.getStartDate())
                .content(projectVo.getContent())
                .applicants(projectVo.getApplicants())
                .createdDate(projectVo.getCreatedDate())
                .build();
    }

}
