package com.auction.flab.application.mapper;

import com.auction.flab.application.vo.MemberUpdateVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    boolean isExistedMember(long id);

    int updateMember(MemberUpdateVo memberUpdateVo);

    int deleteMember(long id);

}
