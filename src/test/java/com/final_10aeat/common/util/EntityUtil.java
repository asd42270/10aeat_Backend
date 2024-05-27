package com.final_10aeat.common.util;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.admin.entity.Admin;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.office.entity.Office;
import java.time.LocalDateTime;

public class EntityUtil {

    public static final Admin DEFAULT_ADMIN = Admin.builder()
        .id(1L)
        .email("admin@example.com")
        .password("password")
        .build();
    public static final Office DEFAULT_OFFICE = Office.builder()
        .id(1L)
        .officeName("Office Name")
        .address("123 Main St")
        .mapX(123.45)
        .mapY(678.90)
        .build();

    public static final Manager DEFAULT_MANAGER = Manager.builder()
        .id(1L)
        .email("manager@example.com")
        .password("password")
        .name("John Doe")
        .phoneNumber("123-456-7890")
        .lunchBreakStart(LocalDateTime.of(2024, 5, 22, 12, 0))
        .lunchBreakEnd(LocalDateTime.of(2024, 5, 22, 13, 0))
        .managerOffice("Manager Office")
        .affiliation("Affiliation")
        .role(MemberRole.MANAGER)
        .build();

    public static final Member DEFAULT_OWNER = Member.builder()
        .id(1L)
        .email("member@example.com")
        .password("password")
        .name("John Doe")
        .role(MemberRole.OWNER)
        .termAgreed(true)
        .build();

    public static final BuildingInfo DEFAULT_BUILDINGINFO = BuildingInfo.builder()
        .id(1L)
        .dong("A동")
        .ho("1호")
        .build();
}
