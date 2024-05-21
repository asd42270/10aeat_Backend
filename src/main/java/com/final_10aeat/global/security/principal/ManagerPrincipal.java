package com.final_10aeat.global.security.principal;

import com.final_10aeat.domain.manager.entity.Manager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.final_10aeat.global.util.AuthoritiesUtil.MANAGER_AUTHORITIES;

@RequiredArgsConstructor
public class ManagerPrincipal implements UserDetails {

    @Getter
    private final Manager manager;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return MANAGER_AUTHORITIES;
    }

    @Override
    public String getPassword() {
        return manager.getPassword();
    }

    @Override
    public String getUsername() {
        return manager.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
