package com.final_10aeat.domain.office.service;

import com.final_10aeat.domain.office.dto.request.CreateOfficeRequestDto;
import com.final_10aeat.domain.office.dto.request.UpdateOfficeRequestDto;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.office.exception.OfficeNotFoundException;
import com.final_10aeat.domain.office.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminOfficeService {

    private final OfficeRepository officeRepository;

    public void register(
            CreateOfficeRequestDto request
    ) {
        Office office = Office.builder()
                .officeName(request.officeName())
                .address(request.address())
                .mapX(request.mapX())
                .mapY(request.mapY())
                .build();

        officeRepository.save(office);
    }

    public void update(
            Long officeId,
            UpdateOfficeRequestDto request
    ) {
        Office office = officeRepository.findById(officeId)
                .orElseThrow(OfficeNotFoundException::new);

        officeUpdate(office, request);
    }

    private void officeUpdate(Office office, UpdateOfficeRequestDto request) {
        if (request.officeName() != null) {
            office.setOfficeName(request.officeName());
        }
    }
}
