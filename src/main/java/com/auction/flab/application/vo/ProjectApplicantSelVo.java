package com.auction.flab.application.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ProjectApplicantSelVo {

    private long projectId;
    private long applicantId;

    public static ProjectApplicantSelVo from (long projectId, long applicantId) {
        return ProjectApplicantSelVo.builder().projectId(projectId).applicantId(applicantId).build();
    }

}
