package com.auction.flab.application.service;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import com.auction.flab.application.mapper.AuthMapper;
import com.auction.flab.application.util.TokenUtil;
import com.auction.flab.application.vo.AuthVo;
import com.auction.flab.application.web.dto.SignInRequestDto;
import com.auction.flab.application.web.dto.SignUpRequestDto;
import com.auction.flab.application.web.dto.SignUpResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthMapper authMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenUtil tokenUtil;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        AuthVo authVo = AuthVo.from(signUpRequestDto);
        if (authMapper.isExistedMember(authVo.getEmail())) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_INPUT_MEMBER);
        }
        authVo.setPassword(bCryptPasswordEncoder.encode(authVo.getPassword()));
        authMapper.insertMember(authVo);
        if (authVo.getId() == null) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_INPUT_MEMBER);
        }
        return SignUpResponseDto.from(authVo.getId());
    }

    @Transactional(readOnly = true)
    public String signIn(SignInRequestDto signInRequestDto) {
        AuthVo authVo = AuthVo.from(signInRequestDto);
        AuthVo result = Optional.ofNullable(authMapper.selectMemberByEmail(authVo.getEmail()))
                .orElseThrow(() -> new ProjectException(ErrorCode.EXCEPTION_ON_LOGIN));
        if (!bCryptPasswordEncoder.matches(authVo.getPassword(), result.getPassword())) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_LOGIN);
        }
        return tokenUtil.createNewAccessToken(authVo);
    }

}
