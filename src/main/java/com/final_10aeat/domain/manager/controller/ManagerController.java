package com.final_10aeat.domain.manager.controller;

import com.final_10aeat.domain.manager.dto.request.CreateManagerRequestDto;
import com.final_10aeat.domain.manager.dto.response.GetManagerResponseDto;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.manager.service.ManagerService;
import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> register(
        @RequestBody @Valid CreateManagerRequestDto request) {
        managerService.register(request);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PostMapping("/login")
    public ResponseDTO<Void> login(
        HttpServletResponse response,
        @RequestBody @Valid MemberLoginRequestDto request) {
        String token = managerService.login(request);
        response.setHeader("accessToken", token);
        return ResponseDTO.ok();
    }

    @GetMapping("/profile/{managerId}")
    public ResponseEntity<ResponseDTO<GetManagerResponseDto>> getManagerProfile(
        @PathVariable Long managerId) {
        Manager manager = managerService.getManagerById(managerId);
        return ResponseEntity.ok(ResponseDTO.okWithData(manager.toDto()));
    }
}
