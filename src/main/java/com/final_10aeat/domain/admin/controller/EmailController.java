package com.final_10aeat.domain.admin.controller;

import com.final_10aeat.domain.admin.dto.request.EmailRequestDto;
import com.final_10aeat.domain.admin.dto.request.EmailVerificationRequestDto;
import com.final_10aeat.domain.admin.dto.response.EmailVerificationResponseDto;
import com.final_10aeat.domain.admin.service.EmailUseCase;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/email")
public class EmailController {

    private final EmailUseCase emailUseCase;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> mailConfirm(
        @RequestBody @Valid EmailRequestDto emailRequest) throws Exception {
        emailUseCase.sendVerificationEmail(emailRequest.email(), emailRequest.role(),
            emailRequest.dong(), emailRequest.ho());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PostMapping("/verification")
    public ResponseEntity<ResponseDTO<EmailVerificationResponseDto>> verifyEmail(
        @RequestBody @Valid EmailVerificationRequestDto verificationRequest) {
        EmailVerificationResponseDto responseDto = emailUseCase.verifyEmailCode(
            verificationRequest.email(), verificationRequest.code());
        return ResponseEntity.ok(ResponseDTO.okWithData(responseDto));
    }
}
