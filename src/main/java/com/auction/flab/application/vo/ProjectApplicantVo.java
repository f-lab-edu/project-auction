package com.auction.flab.application.vo;

import com.auction.flab.application.mapper.ProjectApplicantStatus;
import com.auction.flab.application.web.dto.ProjectApplicantRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectApplicantVo {

    private Long projectId;
    private Long applicantId;
    private String name;
    private String email;
    private int amount;
    private int period;
    private String content;
    private int orderAmount;
    private int executionPeriod;
    private String supportContent;
    private ProjectApplicantStatus status;

    public static ProjectApplicantVo from (Long projectId, Long applicantId) {
        return ProjectApplicantVo.builder().projectId(projectId).applicantId(applicantId).build();
    }

    public static ProjectApplicantVo from (Long projectId, Long applicantId, ProjectApplicantStatus status) {
        return ProjectApplicantVo.builder()
                .projectId(projectId)
                .applicantId(applicantId)
                .status(status)
                .build();
    }

    public static ProjectApplicantVo from (Long projectId, ProjectApplicantRequestDto projectApplicantRequestDto) {
        return ProjectApplicantVo.builder()
                .projectId(projectId)
                .applicantId(projectApplicantRequestDto.getApplicantId())
                .amount(projectApplicantRequestDto.getAmount())
                .period(projectApplicantRequestDto.getPeriod())
                .content(projectApplicantRequestDto.getContent())
                .status(ProjectApplicantStatus.APPLYING)
                .build();
    }

}
