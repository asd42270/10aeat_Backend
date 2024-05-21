package com.final_10aeat.domain.office.controller;

import com.final_10aeat.domain.office.dto.request.RegisterOfficeRequestDto;
import com.final_10aeat.domain.office.dto.request.UpdateOfficeRequestDto;
import com.final_10aeat.domain.office.service.OfficeService;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/offices")
public class OfficeController {

    private final OfficeService officeService;

    @PostMapping
    public ResponseDTO<Void> register(
            @RequestBody @Valid RegisterOfficeRequestDto request
    ) {
        officeService.register(request);
        return ResponseDTO.ok();
    }

    @PatchMapping("/{officeId}")
    public ResponseDTO<Void> update(
            @PathVariable Long officeId,
            @RequestBody UpdateOfficeRequestDto request
    ) {
        officeService.update(officeId, request);
        return ResponseDTO.ok();
    }
}
