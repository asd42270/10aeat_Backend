package com.final_10aeat.common.util.admin;

import static com.final_10aeat.common.util.EntityUtil.DEFAULT_ADMIN;

import com.final_10aeat.domain.admin.entity.Admin;
import com.final_10aeat.global.security.principal.AdminPrincipal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithAdminSecurityContextFactory implements WithSecurityContextFactory<WithAdmin> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd'T'HH:mm:ss");
    private static final Admin defaultAdmin = DEFAULT_ADMIN;

    @Override
    public SecurityContext createSecurityContext(WithAdmin annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Admin admin = buildAdmin(annotation);

        AdminPrincipal principal = new AdminPrincipal(admin);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            principal, null, principal.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }

    private Admin buildAdmin(WithAdmin annotation) {
        return Admin.builder()
            .id(annotation.id() == 0L ? defaultAdmin.getId() : annotation.id())
            .email(nullIfEmpty(annotation.email(), defaultAdmin.getEmail()))
            .password(nullIfEmpty(annotation.password(), defaultAdmin.getPassword()))
            .build();
    }


    private String nullIfEmpty(String value, String defaultValue) {
        return value.isEmpty() ? defaultValue : value;
    }

    private LocalDateTime parseDate(String dateStr, DateTimeFormatter formatter,
        LocalDateTime defaultDate) {
        return dateStr.isEmpty() ? defaultDate : LocalDateTime.parse(dateStr, formatter);
    }
}
