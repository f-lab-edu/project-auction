package com.auction.flab.application.mapper;

import com.auction.flab.application.vo.AuthVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    boolean isExistedMember(String email);

    AuthVo selectMemberByEmail(String email);

    int insertMember(AuthVo authVo);

}
