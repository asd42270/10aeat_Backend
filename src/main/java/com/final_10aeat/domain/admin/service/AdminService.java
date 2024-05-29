package com.final_10aeat.domain.admin.service;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.admin.dto.CreateAdminRequestDto;
import com.final_10aeat.domain.admin.entity.Admin;
import com.final_10aeat.domain.admin.repository.AdminRepository;
import com.final_10aeat.domain.member.dto.request.LoginRequestDto;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Transactional
    public void createAndSaveAdmin(CreateAdminRequestDto request) {
        Admin admin = Admin.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(MemberRole.ADMIN)
            .build();
        adminRepository.save(admin);
    }

    public String login(LoginRequestDto request) {
        Admin admin = adminRepository.findByEmail(request.email())
            .orElseThrow(UserNotExistException::new);

        if (!passwordEncoder.matches(request.password(), admin.getPassword())) {
            throw new UserNotExistException();
        }
        return jwtTokenGenerator.createJwtToken(admin.getEmail(), admin.getRole());
    }
}
