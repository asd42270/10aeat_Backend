package com.final_10aeat.global.security.principal;

import com.final_10aeat.domain.admin.repository.AdminRepository;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDetailsProvider implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return new AdminPrincipal(adminRepository.findByEmail(email)
                .orElseThrow(UserNotExistException::new));
    }
}
