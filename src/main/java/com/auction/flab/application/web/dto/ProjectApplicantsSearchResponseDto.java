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
public class ProjectApplicantsSearchResponseDto {

    private Long projectId;
    private Long applicantId;
    private int amount;
    private int period;
    private String email;

    public static ProjectApplicantsSearchResponseDto from(ProjectApplicantVo projectApplicantVo) {
        return ProjectApplicantsSearchResponseDto.builder()
                .projectId(projectApplicantVo.getProjectId())
                .applicantId(projectApplicantVo.getApplicantId())
                .email(projectApplicantVo.getEmail())
                .amount(projectApplicantVo.getAmount())
                .period(projectApplicantVo.getPeriod())
                .build();
    }

}
