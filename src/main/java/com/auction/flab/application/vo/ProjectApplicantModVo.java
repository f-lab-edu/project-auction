package com.auction.flab.application.vo;

import com.auction.flab.application.mapper.ProjectApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ProjectApplicantModVo {

    private long projectId;
    private long applicantId;
    private ProjectApplicantStatus status;

    public static ProjectApplicantModVo from (long projectId, long applicantId, ProjectApplicantStatus status) {
        return ProjectApplicantModVo.builder()
                .projectId(projectId)
                .applicantId(applicantId)
                .status(status)
                .build();
    }

}
