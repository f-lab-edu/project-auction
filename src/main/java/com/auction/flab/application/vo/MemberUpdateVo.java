package com.auction.flab.application.vo;

import com.auction.flab.application.web.dto.MemberUpdateRequestDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberUpdateVo {

    private long id;
    private String name;
    private String password;
    private String mobileNo;

    public static MemberUpdateVo from(MemberUpdateRequestDto memberUpdateRequestDto) {
        return MemberUpdateVo.builder()
                .name(memberUpdateRequestDto.getName())
                .password(memberUpdateRequestDto.getPassword())
                .mobileNo(memberUpdateRequestDto.getMobileNo())
                .build();
    }

}
