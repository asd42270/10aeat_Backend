package com.final_10aeat.common.util.manager;

import static com.final_10aeat.common.util.EntityUtil.DEFAULT_MANAGER;
import static com.final_10aeat.common.util.EntityUtil.DEFAULT_OFFICE;

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

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd'T'HH:mm:ss");
    private static final Office defaultOffice = DEFAULT_OFFICE;
    private static final Manager defaultManager = DEFAULT_MANAGER;

    @Override
    public SecurityContext createSecurityContext(WithManager annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Office office = buildOffice(annotation);
        Manager manager = buildManager(annotation, office);

        ManagerPrincipal principal = new ManagerPrincipal(manager);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            principal, null, principal.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }

    private Office buildOffice(WithManager annotation) {
        return Office.builder()
            .id(annotation.officeId() == 0L ? defaultOffice.getId() : annotation.officeId())
            .officeName(nullIfEmpty(annotation.officeName(), defaultOffice.getOfficeName()))
            .address(nullIfEmpty(annotation.address(), defaultOffice.getAddress()))
            .mapX(annotation.mapX() == 0.0 ? defaultOffice.getMapX() : annotation.mapX())
            .mapY(annotation.mapY() == 0.0 ? defaultOffice.getMapY() : annotation.mapY())
            .build();
    }

    private Manager buildManager(WithManager annotation, Office office) {
        return Manager.builder()
            .id(annotation.officeId() == 0L ? defaultManager.getId() : annotation.id())
            .email(nullIfEmpty(annotation.email(), defaultManager.getEmail()))
            .password(nullIfEmpty(annotation.password(), defaultManager.getPassword()))
            .name(nullIfEmpty(annotation.name(), defaultManager.getName()))
            .phoneNumber(nullIfEmpty(annotation.phoneNumber(), defaultManager.getPhoneNumber()))
            .lunchBreakStart(
                parseDate(annotation.lunchBreakStart(), defaultManager.getLunchBreakStart()))
            .lunchBreakEnd(parseDate(annotation.lunchBreakEnd(), defaultManager.getLunchBreakEnd()))
            .managerOffice(
                nullIfEmpty(annotation.managerOffice(), defaultManager.getManagerOffice()))
            .affiliation(nullIfEmpty(annotation.affiliation(), defaultManager.getAffiliation()))
            .role(annotation.role().isEmpty() ? defaultManager.getRole()
                : MemberRole.valueOf(annotation.role()))
            .office(office)
            .build();
    }

    private String nullIfEmpty(String value, String defaultValue) {
        return value.isEmpty() ? defaultValue : value;
    }

    private LocalDateTime parseDate(String dateStr, LocalDateTime defaultDate) {
        return dateStr.isEmpty() ? defaultDate : LocalDateTime.parse(dateStr, formatter);
    }
}
