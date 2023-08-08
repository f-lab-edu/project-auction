package com.auction.flab.application.web.dto;

import com.auction.flab.application.validator.DateTimeOrder;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@DateTimeOrder(previousDateTime = "deadline", laterDateTime = "startDate")
@Builder
@Data
public class ProjectRequestDto {

    @NotNull
    private Long proposerId;

    @NotBlank
    private String name;

    @Range(min = 1, max = 100_000)
    @NotNull
    private Integer amount;

    @Range(min = 1, max = 1_000)
    @NotNull
    private Integer period;

    @Future
    @DateTimeFormat
    @NotNull
    private LocalDateTime deadline;

    @Future
    @DateTimeFormat
    @NotNull
    private LocalDateTime startDate;

    @Length(max = 2000)
    @NotBlank
    private String content;

}
