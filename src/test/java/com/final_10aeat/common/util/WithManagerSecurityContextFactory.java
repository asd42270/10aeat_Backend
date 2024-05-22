package com.final_10aeat.common.util;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithManagerSecurityContextFactory implements WithSecurityContextFactory<WithManager> {

    @Override
    public SecurityContext createSecurityContext(WithManager annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        Office office = Office.builder()
            .id(annotation.officeId())
            .officeName(nullIfEmpty(annotation.officeName()))
            .address(nullIfEmpty(annotation.address()))
            .mapX(annotation.mapX())
            .mapY(annotation.mapY())
            .build();

        Manager manager = Manager.builder()
            .email(nullIfEmpty(annotation.email()))
            .password(nullIfEmpty(annotation.password()))
            .name(nullIfEmpty(annotation.name()))
            .phoneNumber(nullIfEmpty(annotation.phoneNumber()))
            .lunchBreakStart(parseDate(annotation.lunchBreakStart(), formatter))
            .lunchBreakEnd(parseDate(annotation.lunchBreakEnd(), formatter))
            .managerOffice(nullIfEmpty(annotation.managerOffice()))
            .affiliation(nullIfEmpty(annotation.affiliation()))
            .role(annotation.role().isEmpty() ? MemberRole.MANAGER : MemberRole.valueOf(annotation.role()))
            .office(office)
            .build();

        ManagerPrincipal principal = new ManagerPrincipal(manager);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            principal, null, principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }

    private String nullIfEmpty(String value) {
        return value.isEmpty() ? null : value;
    }

    private LocalDateTime parseDate(String dateStr, DateTimeFormatter formatter) {
        return dateStr.isEmpty() ? null : LocalDateTime.parse(dateStr, formatter);
    }
}
