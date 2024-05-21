package com.final_10aeat.domain.admin.service;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.admin.entity.Admin;
import com.final_10aeat.domain.admin.repository.AdminRepository;
import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private final JwtTokenGenerator jwtTokenGenerator;

    public String login(MemberLoginRequestDto request) {
        Admin admin = adminRepository.findByEmail(request.email())
                .orElseThrow(UserNotExistException::new);

        if (request.password().equals(admin.getPassword())) {
            //서버용 계정으로 일단 암호화 X
            throw new UserNotExistException();
        }

        return jwtTokenGenerator.createJwtToken(admin.getEmail(), MemberRole.ADMIN);
    }
}
