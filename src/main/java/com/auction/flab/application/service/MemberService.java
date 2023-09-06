package com.auction.flab.application.service;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import com.auction.flab.application.mapper.MemberMapper;
import com.auction.flab.application.vo.MemberUpdateVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void modifyMember(long id, MemberUpdateVo memberUpdateVo) {
        if (!memberMapper.isExistedMember(id)) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND);
        }
        memberUpdateVo.setId(id);
        memberUpdateVo.setPassword(bCryptPasswordEncoder.encode(memberUpdateVo.getPassword()));
        if (memberMapper.updateMember(memberUpdateVo) != 1) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_UPDATE_MEMBER);
        }
    }

    @Transactional
    public void removeMember(long id) {
        if (!memberMapper.isExistedMember(id)) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_NOT_FOUND);
        }
        if (memberMapper.deleteMember(id) != 1) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_DELETE_MEMBER);
        }
    }

}
