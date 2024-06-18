package com.final_10aeat.domain.manager.controller;

import com.final_10aeat.domain.manager.dto.request.EmailRequestDto;
import com.final_10aeat.domain.manager.dto.request.VerifyEmailRequestDto;
import com.final_10aeat.domain.manager.dto.response.VerifyEmailResponseDto;
import com.final_10aeat.domain.manager.service.EmailUseCase;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ResponseDTO<Void>> mailConfirm(
        @RequestBody @Valid EmailRequestDto emailRequest
    ) throws Exception {

        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        Long officeId = principal.getManager().getOffice().getId();
        emailUseCase.sendVerificationEmail(emailRequest.email(), emailRequest.role(),
            emailRequest.dong(), emailRequest.ho(), officeId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PostMapping("/verification")
    public ResponseEntity<ResponseDTO<VerifyEmailResponseDto>> verifyEmail(
        @RequestBody @Valid VerifyEmailRequestDto verificationRequest) {
        VerifyEmailResponseDto responseDto = emailUseCase.verifyEmailCode(
            verificationRequest.email(), verificationRequest.code());
        return ResponseEntity.ok(ResponseDTO.okWithData(responseDto));
    }
}
