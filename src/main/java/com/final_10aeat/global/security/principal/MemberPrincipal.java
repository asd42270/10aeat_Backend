package com.final_10aeat.global.security.principal;

import com.final_10aeat.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.final_10aeat.global.util.AuthoritiesUtil.MEMBER_AUTHORITIES;

@RequiredArgsConstructor
public class MemberPrincipal implements UserDetails {

    @Getter
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return MEMBER_AUTHORITIES;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
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
