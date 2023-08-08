package com.auction.flab.application.vo;

import com.auction.flab.application.web.dto.SignInRequestDto;
import com.auction.flab.application.web.dto.SignUpRequestDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthVo {

    private Long id;
    private String email;
    private String name;
    private String password;
    private String mobileNo;

    public static AuthVo from(SignUpRequestDto signUpRequestDto) {
        return AuthVo.builder()
                .email(signUpRequestDto.getEmail())
                .name(signUpRequestDto.getName())
                .password(signUpRequestDto.getPassword())
                .mobileNo(signUpRequestDto.getMobileNo())
                .build();
    }

    public static AuthVo from(SignInRequestDto signInRequestDto) {
        return AuthVo.builder()
                .email(signInRequestDto.getEmail())
                .password(signInRequestDto.getPassword())
                .build();
    }

}
