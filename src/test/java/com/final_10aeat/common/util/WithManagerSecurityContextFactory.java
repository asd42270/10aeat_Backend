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

        Office defaultOffice = Office.builder()
            .id(1L)
            .officeName("Office Name")
            .address("123 Main St")
            .mapX(123.45)
            .mapY(678.90)
            .build();

        Manager defaultManager = Manager.builder()
            .email("manager@example.com")
            .password("password")
            .name("John Doe")
            .phoneNumber("123-456-7890")
            .lunchBreakStart(LocalDateTime.of(2024, 5, 22, 12, 0))
            .lunchBreakEnd(LocalDateTime.of(2024, 5, 22, 13, 0))
            .managerOffice("Manager Office")
            .affiliation("Affiliation")
            .role(MemberRole.MANAGER)
            .office(defaultOffice)
            .build();

        // 어노테이션 속성 값이 빈 문자열이면 기본값 사용
        Office office = Office.builder()
            .id(annotation.officeId() == 0L ? defaultOffice.getId() : annotation.officeId())
            .officeName(nullIfEmpty(annotation.officeName(), defaultOffice.getOfficeName()))
            .address(nullIfEmpty(annotation.address(), defaultOffice.getAddress()))
            .mapX(annotation.mapX() == 0.0 ? defaultOffice.getMapX() : annotation.mapX())
            .mapY(annotation.mapY() == 0.0 ? defaultOffice.getMapY() : annotation.mapY())
            .build();

        Manager manager = Manager.builder()
            .email(nullIfEmpty(annotation.email(), defaultManager.getEmail()))
            .password(nullIfEmpty(annotation.password(), defaultManager.getPassword()))
            .name(nullIfEmpty(annotation.name(), defaultManager.getName()))
            .phoneNumber(nullIfEmpty(annotation.phoneNumber(), defaultManager.getPhoneNumber()))
            .lunchBreakStart(parseDate(annotation.lunchBreakStart(), formatter,
                defaultManager.getLunchBreakStart()))
            .lunchBreakEnd(
                parseDate(annotation.lunchBreakEnd(), formatter, defaultManager.getLunchBreakEnd()))
            .managerOffice(
                nullIfEmpty(annotation.managerOffice(), defaultManager.getManagerOffice()))
            .affiliation(nullIfEmpty(annotation.affiliation(), defaultManager.getAffiliation()))
            .role(annotation.role().isEmpty() ? defaultManager.getRole()
                : MemberRole.valueOf(annotation.role()))
            .office(office)
            .build();

        ManagerPrincipal principal = new ManagerPrincipal(manager);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            principal, null, principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }

    private String nullIfEmpty(String value, String defaultValue) {
        return value.isEmpty() ? defaultValue : value;
    }

    private LocalDateTime parseDate(String dateStr, DateTimeFormatter formatter,
        LocalDateTime defaultDate) {
        return dateStr.isEmpty() ? defaultDate : LocalDateTime.parse(dateStr, formatter);
    }
}
