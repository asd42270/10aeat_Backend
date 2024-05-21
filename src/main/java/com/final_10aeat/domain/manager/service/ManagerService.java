package com.final_10aeat.domain.manager.service;

import com.final_10aeat.domain.manager.dto.request.CreateAdminRequestDto;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.office.exception.OfficeNotFoundException;
import com.final_10aeat.domain.manager.repository.ManagerRepository;
import com.final_10aeat.domain.office.repository.OfficeRepository;
import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.member.exception.EmailDuplicatedException;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final OfficeRepository officeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;

    public Manager register(CreateAdminRequestDto request) {
        validateEmailNotDuplicated(request.email());
        Office office = validateAndGetOffice(request.officeId());
        return createAndSaveAdmin(request, office);
    }

    private void validateEmailNotDuplicated(String email) {
        if (managerRepository.findByEmail(email).isPresent()) {
            throw new EmailDuplicatedException();
        }
    }

    private Office validateAndGetOffice(long officeId) {
        return officeRepository.findById(officeId)
            .orElseThrow(OfficeNotFoundException::new);
    }

    private Manager createAndSaveAdmin(CreateAdminRequestDto request, Office office) {
        Manager manager = Manager.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .name(request.name())
            .phoneNumber(request.phoneNumber())
            .lunchBreakStart(request.lunchBreakStart())
            .lunchBreakEnd(request.lunchBreakEnd())
            .adminOffice(request.adminOffice())
            .affiliation(request.affiliation())
            .office(office)
            .role(MemberRole.MANAGER)
            .build();

        return managerRepository.save(manager);
    }

    @Transactional(readOnly = true)
    public String login(MemberLoginRequestDto request) {
        Manager manager = managerRepository.findByEmail(request.email())
            .orElseThrow(UserNotExistException::new);

        if (!passwordEncoder.matches(request.password(), manager.getPassword())) {
            throw new UserNotExistException();
        }

        return jwtTokenGenerator.createJwtToken(manager.getEmail(), manager.getRole());
    }
}
