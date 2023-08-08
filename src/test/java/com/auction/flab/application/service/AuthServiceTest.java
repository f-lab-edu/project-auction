package com.auction.flab.application.service;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.InternalException;
import com.auction.flab.application.mapper.AuthMapper;
import com.auction.flab.application.util.TokenUtil;
import com.auction.flab.application.vo.AuthVo;
import com.auction.flab.application.web.dto.SignInRequestDto;
import com.auction.flab.application.web.dto.SignUpRequestDto;
import com.auction.flab.application.web.dto.SignUpResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    AuthMapper authMapper;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    TokenUtil tokenUtil;
    @InjectMocks
    AuthService authService;

    @Test
    void signUp_success() {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        AuthVo authVo = AuthVo.from(signUpRequestDto);
        given(authMapper.insertMember(eq(authVo))).will(invocation -> {
            authVo.setId(1L);
            AuthVo arg = invocation.getArgument(0);
            arg.setId(1L);
            return 1;
        });
        given(bCryptPasswordEncoder.encode(eq(authVo.getPassword()))).willReturn(authVo.getPassword());

        // when
        SignUpResponseDto signUpResponseDto = authService.signUp(signUpRequestDto);

        // then
        assertEquals(1L, signUpResponseDto.getId());
        then(authMapper).should(times(1)).insertMember(eq(authVo));
    }

    @Test
    void singUp_failed_due_to_duplicate_member_exists() {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authMapper.isExistedMember(eq(signUpRequestDto.getEmail()))).willReturn(true);

        // when
        InternalException internalException = assertThrows(InternalException.class, () -> authService.signUp(signUpRequestDto));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_INPUT_MEMBER, internalException.getErrorCode());
        then(authMapper).should(times(1)).isExistedMember(eq(signUpRequestDto.getEmail()));
    }

    @Test
    void signUp_failed_due_to_db_exception() {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password("Aa!1234567")
                .mobileNo("01012345678")
                .build();
        given(authMapper.insertMember(eq(AuthVo.from(signUpRequestDto)))).willReturn(1);
        given(bCryptPasswordEncoder.encode(eq(signUpRequestDto.getPassword()))).willReturn(signUpRequestDto.getPassword());

        // when
        InternalException internalException = assertThrows(InternalException.class, () -> authService.signUp(signUpRequestDto));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_INPUT_MEMBER, internalException.getErrorCode());
        then(authMapper).should(times(1)).insertMember(eq(AuthVo.from(signUpRequestDto)));
    }

    @Test
    void signIn_success() {
        // given
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .email("test@gmail.com")
                .password("Aa!1234567")
                .build();
        AuthVo authVo = AuthVo.from(signInRequestDto);
        given(authMapper.selectMemberByEmail(eq(authVo.getEmail()))).willReturn(authVo);
        given(bCryptPasswordEncoder.matches(eq(authVo.getPassword()), anyString())).willReturn(true);
        given(tokenUtil.createNewAccessToken(eq(authVo))).willReturn("success_token");

        // when
        String token = authService.signIn(signInRequestDto);

        // then
        assertEquals("success_token", token);
        then(authMapper).should(times(1)).selectMemberByEmail(eq(authVo.getEmail()));
    }

    @Test
    void signIn_failed_due_to_wrong_id() {
        // given
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .email("no_account@gmail.com")
                .password("Aa!1234567")
                .build();
        AuthVo authVo = AuthVo.from(signInRequestDto);
        given(authMapper.selectMemberByEmail(eq(authVo.getEmail()))).willReturn(null);

        // when
        InternalException internalException = assertThrows(InternalException.class, () -> authService.signIn(signInRequestDto));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_LOGIN, internalException.getErrorCode());
        then(authMapper).should(times(1)).selectMemberByEmail(eq(authVo.getEmail()));
    }

    @Test
    void signIn_failed_due_to_wrong_password() {
        // given
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .email("test@gmail.com")
                .password("Aa!1234567xxx")
                .build();
        AuthVo authVo = AuthVo.from(signInRequestDto);
        given(authMapper.selectMemberByEmail(eq(authVo.getEmail()))).willReturn(authVo);
        given(bCryptPasswordEncoder.matches(eq(authVo.getPassword()), anyString())).willReturn(false);

        // when
        InternalException internalException = assertThrows(InternalException.class, () -> authService.signIn(signInRequestDto));

        // then
        assertEquals(ErrorCode.EXCEPTION_ON_LOGIN, internalException.getErrorCode());
        then(authMapper).should(times(1)).selectMemberByEmail(eq(authVo.getEmail()));
        then(bCryptPasswordEncoder).should(times(1)).matches(eq(authVo.getPassword()), anyString());
    }

}