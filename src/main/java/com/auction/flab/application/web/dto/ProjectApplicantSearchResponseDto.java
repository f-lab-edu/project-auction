package com.auction.flab.application.web.dto;

import com.auction.flab.application.vo.ProjectApplicantResultVo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
@AllArgsConstructor
public class ProjectApplicantSearchResponseDto {

    private long projectId;
    private long applicantId;
    private String name;
    private int amount;
    private int period;
    private String content;
    private int orderAmount;
    private int executionPeriod;
    private String supportContent;
    private String email;

    public static ProjectApplicantSearchResponseDto from(ProjectApplicantResultVo projectApplicantResultVo) {
        return ProjectApplicantSearchResponseDto.builder()
                .projectId(projectApplicantResultVo.getProjectId())
                .applicantId(projectApplicantResultVo.getApplicantId())
                .name(projectApplicantResultVo.getName())
                .email(projectApplicantResultVo.getEmail())
                .amount(projectApplicantResultVo.getAmount())
                .period(projectApplicantResultVo.getPeriod())
                .content(projectApplicantResultVo.getContent())
                .orderAmount(projectApplicantResultVo.getOrderAmount())
                .executionPeriod(projectApplicantResultVo.getExecutionPeriod())
                .supportContent(projectApplicantResultVo.getSupportContent())
                .build();
    }

}
