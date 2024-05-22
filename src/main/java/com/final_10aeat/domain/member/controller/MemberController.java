package com.final_10aeat.domain.member.controller;

import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberWithdrawRequestDto;
import com.final_10aeat.domain.member.service.MemberService;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseDTO<Void> register(
            HttpServletResponse response,
            @RequestBody @Valid MemberRegisterRequestDto request
    ) {
        MemberLoginRequestDto loginDto = memberService.register(request);

        String token = memberService.login(loginDto);
        response.setHeader("accessToken", token);

        return ResponseDTO.ok();
    }

    @PostMapping("/login")
    public ResponseDTO<Void> login(
            HttpServletResponse response,
            @RequestBody @Valid MemberLoginRequestDto request) {
        String token = memberService.login(request);
        response.setHeader("accessToken", token);
        return ResponseDTO.ok();
    }

    @DeleteMapping
    public ResponseDTO<Void> withdraw(
            @RequestBody @Valid MemberWithdrawRequestDto request
    ){
        memberService.withdraw(request);
        return ResponseDTO.ok();
    }

    @GetMapping
    public ResponseDTO<?> test(
        @AuthenticationPrincipal MemberPrincipal principal
    ){
        return ResponseDTO.okWithData(principal.getMember());
    }
}
