package com.final_10aeat.domain.admin.controller;

import com.final_10aeat.domain.admin.dto.CreateAdminRequestDto;
import com.final_10aeat.domain.admin.service.AdminService;
import com.final_10aeat.common.dto.LoginRequestDto;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> register(
        @RequestBody @Valid CreateAdminRequestDto request) {
        adminService.createAndSaveAdmin(request);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PostMapping("/login")
    public ResponseDTO<Void> login(
        HttpServletResponse response,
        @RequestBody @Valid LoginRequestDto request) {
        String token = adminService.login(request);
        response.setHeader("accessToken", token);
        return ResponseDTO.ok();
    }
}
