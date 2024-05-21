package com.final_10aeat.domain.manager.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.final_10aeat.domain.manager.dto.request.CreateManagerRequestDto;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.office.exception.OfficeNotFoundException;
import com.final_10aeat.domain.manager.repository.ManagerRepository;
import com.final_10aeat.domain.office.repository.OfficeRepository;
import com.final_10aeat.domain.manager.service.ManagerService;
import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.member.exception.EmailDuplicatedException;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenGenerator jwtTokenGenerator;

    @InjectMocks
    private ManagerService managerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("registerManager()은")
    class Context_RegisterManager {


        @Test
        @DisplayName("성공적으로 관리자를 등록한다.")
        void _willSuccess() {
            CreateManagerRequestDto requestDto = new CreateManagerRequestDto(
                "manager@example.com", "securePassword", "김관리", "010-1234-5678",
                LocalDateTime.of(2024, 1, 1, 12, 0),
                LocalDateTime.of(2024, 1, 1, 13, 0),
                "중앙 현관 1층 관리자 사무소", "김씨 관리 협회", 1L
            );

            Office office = Office.builder()
                .id(1L)
                .officeName("미왕 빌딩")
                .address("123 Gangnam St.")
                .mapX("35.6895")
                .mapY("139.6917")
                .build();

            Manager manager = Manager.builder()
                .email("manager@example.com")
                .password("encodedPassword")
                .name("김관리")
                .phoneNumber("010-1234-5678")
                .lunchBreakStart(LocalDateTime.of(2024, 1, 1, 12, 0))
                .lunchBreakEnd(LocalDateTime.of(2024, 1, 1, 13, 0))
                .managerOffice("중앙 현관 1층 관리자 사무소")
                .affiliation("김씨 관리 협회")
                .office(office)
                .role(MemberRole.MANAGER)
                .build();

            when(managerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(officeRepository.findById(anyLong())).thenReturn(Optional.of(office));
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(managerRepository.save(any(Manager.class))).thenReturn(manager);

            Manager result = managerService.register(requestDto);

            verify(managerRepository).save(any(Manager.class));
            assertEquals("manager@example.com", result.getEmail());
            assertEquals("김관리", result.getName());
        }

        @Test
        @DisplayName("이미 존재하는 이메일로 등록을 시도하면 실패한다.")
        void Duplicated_willFail() {
            CreateManagerRequestDto requestDto = new CreateManagerRequestDto(
                "manager@example.com", "securePassword", "김관리", "010-1234-5678",
                LocalDateTime.of(2024, 1, 1, 12, 0),
                LocalDateTime.of(2024, 1, 1, 13, 0),
                "중앙 현관 1층 관리자 사무소", "김씨 관리 협회", 1L
            );

            Manager existingManager = Manager.builder().build();

            when(managerRepository.findByEmail(anyString())).thenReturn(Optional.of(existingManager));

            assertThrows(EmailDuplicatedException.class, () -> {
                managerService.register(requestDto);
            });
        }

        @Test
        @DisplayName("존재하지 않는 사무소 ID로 등록을 시도하면 실패한다.")
        void NotFound_willFail() {
            CreateManagerRequestDto requestDto = new CreateManagerRequestDto(
                "manager@example.com", "securePassword", "김관리", "010-1234-5678",
                LocalDateTime.of(2024, 1, 1, 12, 0),
                LocalDateTime.of(2024, 1, 1, 13, 0),
                "중앙 현관 1층 관리자 사무소", "김씨 관리 협회", 1L
            );

            when(managerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(officeRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(OfficeNotFoundException.class, () -> {
                managerService.register(requestDto);
            });
        }
    }

    @Nested
    @DisplayName("login()은")
    class Login {

        @Test
        @DisplayName("성공적으로 로그인을 수행한다.")
        void _willSuccess() {
            MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto("manager@example.com",
                "securePassword");

            Manager manager = Manager.builder()
                .email("manager@example.com")
                .password("encodedPassword")
                .role(MemberRole.MANAGER)
                .build();

            when(managerRepository.findByEmail(anyString())).thenReturn(Optional.of(manager));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(jwtTokenGenerator.createJwtToken(anyString(), any(MemberRole.class))).thenReturn(
                "mockToken");

            String token = managerService.login(loginRequestDto);

            assertEquals("mockToken", token);
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 로그인을 시도하면 실패한다.")
        void NotFound_willFail() {
            MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto("manager@example.com",
                "securePassword");

            when(managerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            assertThrows(UserNotExistException.class, () -> {
                managerService.login(loginRequestDto);
            });
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인을 시도하면 실패한다.")
        void NotMatch_willFail() {
            MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto("manager@example.com",
                "securePassword");

            Manager manager = Manager.builder()
                .email("manager@example.com")
                .password("encodedPassword")
                .role(MemberRole.MANAGER)
                .build();

            when(managerRepository.findByEmail(anyString())).thenReturn(Optional.of(manager));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

            assertThrows(UserNotExistException.class, () -> {
                managerService.login(loginRequestDto);
            });
        }
    }
}
