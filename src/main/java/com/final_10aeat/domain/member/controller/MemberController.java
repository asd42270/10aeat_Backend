package com.final_10aeat.domain.member.controller;

import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRequestDto;
import com.final_10aeat.domain.member.service.MemberService;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseDTO<Void> register(
            @RequestBody MemberRegisterRequestDto request
    ){
        memberService.register(request);
        return ResponseDTO.ok();
    }

    @PostMapping("/login")
    public ResponseDTO<Void> login(
            HttpServletResponse response,
            @RequestBody MemberRequestDto request){
        String token = memberService.login(request);
        response.setHeader(HttpHeaders.AUTHORIZATION, token);
        return ResponseDTO.ok();
    }
}
