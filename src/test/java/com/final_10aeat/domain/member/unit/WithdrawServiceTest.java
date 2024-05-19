package com.final_10aeat.domain.member.unit;

import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberWithdrawRequestDto;
import com.final_10aeat.domain.member.entity.MemberRole;
import com.final_10aeat.domain.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class WithdrawServiceTest {

    @Mock
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        String email = "spring";
        String password= "spring";
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(email, password, MemberRole.TENANT);
        memberService.register(requestDto);
    }

    @Test
    @DisplayName("회원 탈퇴 메서드가 정상 작동한다.")
    void _willSuccess() {
        String email = "spring";
        String password = "spring";
        MemberWithdrawRequestDto memberRequest = new MemberWithdrawRequestDto(email, password);

        memberService.withdraw(memberRequest);

        verify(memberService).withdraw(memberRequest);
    }
}
