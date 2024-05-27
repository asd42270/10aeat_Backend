package com.final_10aeat.common.util.member;

import static com.final_10aeat.common.util.EntityUtil.DEFAULT_BUILDINGINFO;
import static com.final_10aeat.common.util.EntityUtil.DEFAULT_OFFICE;
import static com.final_10aeat.common.util.EntityUtil.DEFAULT_OWNER;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMemberSecurityContextFactory implements WithSecurityContextFactory<WithMember> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd'T'HH:mm:ss");
    private static final Office defaultOffice = DEFAULT_OFFICE;
    private static final Member defaultMember = DEFAULT_OWNER;
    private static final BuildingInfo defaultBuildingInfo = DEFAULT_BUILDINGINFO;

    @Override
    public SecurityContext createSecurityContext(
        WithMember annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Office office = buildOffice(annotation);
        BuildingInfo buildingInfo = buildBuildingInfo(annotation, office);
        Member member = buildMember(annotation, office, buildingInfo);

        MemberPrincipal principal = new MemberPrincipal(member);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            principal, null, principal.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }

    private Office buildOffice(WithMember annotation) {
        return Office.builder()
            .id(annotation.officeId() == 0L ? defaultOffice.getId() : annotation.officeId())
            .officeName(nullIfEmpty(annotation.officeName(), defaultOffice.getOfficeName()))
            .address(nullIfEmpty(annotation.address(), defaultOffice.getAddress()))
            .mapX(annotation.mapX() == 0.0 ? defaultOffice.getMapX() : annotation.mapX())
            .mapY(annotation.mapY() == 0.0 ? defaultOffice.getMapY() : annotation.mapY())
            .build();
    }

    private BuildingInfo buildBuildingInfo(WithMember annotation, Office office) {
        return BuildingInfo.builder()
            .id(annotation.buildingInfoId() == 0L ? defaultBuildingInfo.getId()
                : annotation.buildingInfoId())
            .dong(nullIfEmpty(annotation.buildingInfoDong(), defaultBuildingInfo.getDong()))
            .ho(nullIfEmpty(annotation.buildingInfoHo(), defaultBuildingInfo.getHo()))
            .office(office)
            .build();
    }

    private Member buildMember(WithMember annotation, Office office, BuildingInfo buildingInfo) {
        return Member.builder()
            .id(annotation.officeId() == 0L ? defaultMember.getId() : annotation.officeId())
            .email(nullIfEmpty(annotation.email(), defaultMember.getEmail()))
            .password(nullIfEmpty(annotation.password(), defaultMember.getPassword()))
            .name(nullIfEmpty(annotation.name(), defaultMember.getName()))
            .role(MemberRole.OWNER)
            .termAgreed(true)
            .offices(Set.of(office))
            .buildingInfos(Set.of(buildingInfo))
            .build();
    }

    private String nullIfEmpty(String value, String defaultValue) {
        return value.isEmpty() ? defaultValue : value;
    }

    private LocalDateTime parseDate(String dateStr, LocalDateTime defaultDate) {
        return dateStr.isEmpty() ? defaultDate : LocalDateTime.parse(dateStr, formatter);
    }
}
