package com.auction.flab.application.web.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProjectResponseDto {

    private final Long id;

    public static ProjectResponseDto from(long id) {
        return new ProjectResponseDto(id);
    }

}
