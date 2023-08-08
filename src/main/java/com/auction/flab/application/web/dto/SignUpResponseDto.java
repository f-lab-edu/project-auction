package com.auction.flab.application.web.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignUpResponseDto {

    private final Long id;

    public static SignUpResponseDto from(long id) {
        return new SignUpResponseDto(id);
    }

}
