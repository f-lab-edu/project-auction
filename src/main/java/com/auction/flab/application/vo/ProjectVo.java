package com.auction.flab.application.vo;

import com.auction.flab.application.mapper.ProjectStatus;
import com.auction.flab.application.web.dto.ProjectRequestDto;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVo {

    private Long id;
    private Long proposerId;
    private String name;
    private int amount;
    private int period;
    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private String content;
    private ProjectStatus status;
    private int applicants;
    private LocalDateTime createdDate;

    public static ProjectVo from(ProjectRequestDto projectRequestDto) {
        return ProjectVo.builder()
                .proposerId(projectRequestDto.getProposerId())
                .name(projectRequestDto.getName())
                .amount(projectRequestDto.getAmount())
                .period(projectRequestDto.getPeriod())
                .deadline(projectRequestDto.getDeadline())
                .startDate(projectRequestDto.getStartDate())
                .content(projectRequestDto.getContent())
                .status(ProjectStatus.PROPOSAL)
                .build();
    }

}
