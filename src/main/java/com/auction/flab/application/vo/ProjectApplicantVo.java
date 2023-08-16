package com.auction.flab.application.vo;

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
    private int amount;
    private int period;
    private String content;
    private int orderAmount;
    private int executionPeriod;
    private String supportContent;
    private String email;

    public static ProjectApplicantVo from (Long projectId, Long applicantId) {
        return ProjectApplicantVo.builder().projectId(projectId).applicantId(applicantId).build();
    }

}
