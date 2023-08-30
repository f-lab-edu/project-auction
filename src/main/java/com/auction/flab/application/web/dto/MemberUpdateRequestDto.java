package com.auction.flab.application.web.dto;

import com.auction.flab.application.validator.Password;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
public class MemberUpdateRequestDto {

    @Length(max = 10)
    @NotBlank
    private String name;

    @Password
    private String password;

    @Length(max = 13)
    @Positive
    @NotBlank
    private String mobileNo;

}
