package com.auction.flab.application.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
public class PageVo {

    private int page;
    private int size;
    private int totalCount;

    public static PageVo from(int page, int size) {
        return PageVo.builder().page(page - 1).size(size).build();
    }

}
