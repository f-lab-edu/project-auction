package com.auction.flab.application.vo;

import com.auction.flab.application.mapper.ProjectApplicantStatus;
import com.auction.flab.application.web.dto.ProjectApplicantRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ProjectApplicantAddVo {

    private long projectId;
    private long applicantId;
    private int amount;
    private int period;
    private String content;
    private ProjectApplicantStatus status;

    public static ProjectApplicantAddVo from (long projectId, ProjectApplicantRequestDto projectApplicantRequestDto) {
        return ProjectApplicantAddVo.builder()
                .projectId(projectId)
                .applicantId(projectApplicantRequestDto.getApplicantId())
                .amount(projectApplicantRequestDto.getAmount())
                .period(projectApplicantRequestDto.getPeriod())
                .content(projectApplicantRequestDto.getContent())
                .status(ProjectApplicantStatus.APPLYING)
                .build();
    }

}
