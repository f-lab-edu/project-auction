package com.auction.flab.application.web.dto;

import com.auction.flab.application.vo.ProjectApplicantVo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectApplicantSearchResponseDto {

    private Long projectId;
    private Long applicantId;
    private String name;
    private int amount;
    private int period;
    private String content;
    private int orderAmount;
    private int executionPeriod;
    private String supportContent;
    private String email;

    public static ProjectApplicantSearchResponseDto from(ProjectApplicantVo projectApplicantVo) {
        return ProjectApplicantSearchResponseDto.builder()
                .projectId(projectApplicantVo.getProjectId())
                .applicantId(projectApplicantVo.getApplicantId())
                .name(projectApplicantVo.getName())
                .email(projectApplicantVo.getEmail())
                .amount(projectApplicantVo.getAmount())
                .period(projectApplicantVo.getPeriod())
                .content(projectApplicantVo.getContent())
                .orderAmount(projectApplicantVo.getOrderAmount())
                .executionPeriod(projectApplicantVo.getExecutionPeriod())
                .supportContent(projectApplicantVo.getSupportContent())
                .build();
    }

}
