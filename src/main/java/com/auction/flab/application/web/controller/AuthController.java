package com.auction.flab.application.web.controller;

import com.auction.flab.application.service.AuthService;
import com.auction.flab.application.web.dto.SignInRequestDto;
import com.auction.flab.application.web.dto.SignInResponseDto;
import com.auction.flab.application.web.dto.SignUpRequestDto;
import com.auction.flab.application.web.dto.SignUpResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = authService.signUp(signUpRequestDto);
        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@Valid @RequestBody SignInRequestDto signInRequestDto) {
        String accessToken = authService.signIn(signInRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SignInResponseDto(accessToken));
    }

}
