package com.final_10aeat.global.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AuthoritiesUtil {

    public static final Collection<SimpleGrantedAuthority> ADMIN_AUTHORITIES = Arrays.asList(
            new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("ROLE_MANAGER"),
            new SimpleGrantedAuthority("ROLE_ADMIN")
    );
    public static final Collection<SimpleGrantedAuthority> MANAGER_AUTHORITIES = Arrays.asList(
            new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("ROLE_MANAGER")
    );
    public static final Collection<SimpleGrantedAuthority> MEMBER_AUTHORITIES = List.of(
            new SimpleGrantedAuthority("ROLE_USER")
    );
}
