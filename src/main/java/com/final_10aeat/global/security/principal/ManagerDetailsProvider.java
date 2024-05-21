package com.final_10aeat.global.security.principal;

import com.final_10aeat.domain.manager.repository.ManagerRepository;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerDetailsProvider implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return new ManagerPrincipal(managerRepository.findByEmail(email)
                .orElseThrow(UserNotExistException::new));
    }
}
