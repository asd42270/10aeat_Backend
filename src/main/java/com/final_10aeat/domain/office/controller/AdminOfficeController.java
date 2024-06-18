package com.final_10aeat.domain.office.controller;

import com.final_10aeat.domain.office.dto.request.CreateOfficeRequestDto;
import com.final_10aeat.domain.office.dto.request.UpdateOfficeRequestDto;
import com.final_10aeat.domain.office.service.AdminOfficeService;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/offices")
public class AdminOfficeController {

    private final AdminOfficeService adminOfficeService;

    @PostMapping
    public ResponseDTO<Void> register(
            @RequestBody @Valid CreateOfficeRequestDto request
    ) {
        adminOfficeService.register(request);
        return ResponseDTO.ok();
    }

    @PatchMapping("/{officeId}")
    public ResponseDTO<Void> update(
            @PathVariable Long officeId,
            @RequestBody UpdateOfficeRequestDto request
    ) {
        adminOfficeService.update(officeId, request);
        return ResponseDTO.ok();
    }
}
