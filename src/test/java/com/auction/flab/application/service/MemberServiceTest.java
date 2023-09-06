package com.auction.flab.application.service;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import com.auction.flab.application.mapper.MemberMapper;
import com.auction.flab.application.vo.MemberUpdateVo;
import com.auction.flab.application.web.dto.MemberUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberMapper memberMapper;
    @InjectMocks
    MemberService memberService;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("회원 정보 수정 성공")
    @Test
    void update_member_success() {
        // given
        long id = 1L;
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!1234567update")
                .mobileNo("01099998888")
                .build();
        MemberUpdateVo memberUpdateVo = MemberUpdateVo.from(memberUpdateRequestDto);
        given(memberMapper.isExistedMember(eq(id))).willReturn(true);
        given(memberMapper.updateMember(eq(memberUpdateVo))).willReturn(1);

        // when
        memberService.modifyMember(id, memberUpdateVo);

        // then
        then(memberMapper).should(times(1)).updateMember(eq(memberUpdateVo));
    }

    @DisplayName("회원 정보 수정 실패 - 회원 미존재")
    @Test
    void update_member_fail_due_to_no_member() {
        // given
        long id = 1L;
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!1234567update")
                .mobileNo("01099998888")
                .build();
        MemberUpdateVo memberUpdateVo = MemberUpdateVo.from(memberUpdateRequestDto);
        given(memberMapper.isExistedMember(eq(id))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> memberService.modifyMember(id, memberUpdateVo));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(memberMapper).should(times(0)).updateMember(eq(memberUpdateVo));
    }

    @DisplayName("회원 정보 수정 실패 - DB 에러")
    @Test
    void update_member_fail_due_to_db_exception() {
        // given
        long id = 1L;
        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .name("김철수")
                .password("Aa!1234567update")
                .mobileNo("01099998888")
                .build();
        MemberUpdateVo memberUpdateVo = MemberUpdateVo.from(memberUpdateRequestDto);
        given(memberMapper.isExistedMember(eq(id))).willReturn(true);
        given(memberMapper.updateMember(eq(memberUpdateVo))).willReturn(0);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> memberService.modifyMember(id, memberUpdateVo));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_UPDATE_MEMBER, projectException.getErrorCode());
        then(memberMapper).should(times(1)).updateMember(eq(memberUpdateVo));
    }

    @DisplayName("회원 정보 삭제 성공")
    @Test
    void delete_member_success() {
        // given
        long id = 1L;
        given(memberMapper.isExistedMember(eq(id))).willReturn(true);
        given(memberMapper.deleteMember(eq(id))).willReturn(1);

        // when
        memberService.removeMember(id);

        // then
        then(memberMapper).should(times(1)).deleteMember(eq(id));
    }

    @DisplayName("회원 정보 수정 실패 - 회원 미존재")
    @Test
    void delete_member_fail_due_to_no_member() {
        // given
        long id = 1L;
        given(memberMapper.isExistedMember(eq(id))).willReturn(false);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> memberService.removeMember(id));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_NOT_FOUND, projectException.getErrorCode());
        then(memberMapper).should(times(0)).deleteMember(eq(id));
    }

    @DisplayName("회원 정보 수정 실패 - DB 에러")
    @Test
    void delete_member_fail_due_to_db_exception() {
        // given
        long id = 1L;
        given(memberMapper.isExistedMember(eq(id))).willReturn(true);
        given(memberMapper.deleteMember(eq(id))).willReturn(0);

        // when
        ProjectException projectException = assertThrows(ProjectException.class, () -> memberService.removeMember(id));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_DELETE_MEMBER, projectException.getErrorCode());
        then(memberMapper).should(times(1)).deleteMember(eq(id));
    }

}