package com.auction.flab.application.vo;

import lombok.Builder;
import lombok.Data;

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
