package com.auction.flab.application.web.dto;

import com.auction.flab.application.validator.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
public class SignInRequestDto {

    @Length(max = 30)
    @Email
    @NotBlank
    private String email;

    @Password
    private String password;

}
