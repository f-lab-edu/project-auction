package com.auction.flab.application.web.controller;

import com.auction.flab.application.service.MemberService;
import com.auction.flab.application.vo.MemberUpdateVo;
import com.auction.flab.application.web.dto.MemberUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/members/{id}")
    public ResponseEntity<Void> modifyMember(@PathVariable("id") long id, @Valid @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        MemberUpdateVo memberUpdateVo = MemberUpdateVo.from(memberUpdateRequestDto);
        memberService.modifyMember(id, memberUpdateVo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> removeMember(@PathVariable("id") long id) {
        memberService.removeMember(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
