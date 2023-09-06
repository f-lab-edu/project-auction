package com.auction.flab.application.web.dto;

import com.auction.flab.application.vo.ProjectApplicantResultVo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
public class ProjectApplicantsSearchResponseDto {

    private long projectId;
    private long applicantId;
    private int amount;
    private int period;
    private String email;

    public static ProjectApplicantsSearchResponseDto from(ProjectApplicantResultVo projectApplicantResultVo) {
        return ProjectApplicantsSearchResponseDto.builder()
                .projectId(projectApplicantResultVo.getProjectId())
                .applicantId(projectApplicantResultVo.getApplicantId())
                .email(projectApplicantResultVo.getEmail())
                .amount(projectApplicantResultVo.getAmount())
                .period(projectApplicantResultVo.getPeriod())
                .build();
    }

}
