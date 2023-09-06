package com.auction.flab.application.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
public class ProjectApplicantRequestDto {

    @Positive
    @NotNull
    private Long applicantId;

    @Range(min = 1, max = 100_000)
    private int amount;

    @Range(min = 1, max = 1_000)
    private int period;

    @Length(max = 2000)
    @NotBlank
    private String content;

}
