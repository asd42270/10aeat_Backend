package com.final_10aeat.global.security.jwt;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.global.security.principal.AdminDetailsProvider;
import com.final_10aeat.global.security.principal.ManagerDetailsProvider;
import com.final_10aeat.global.security.principal.MemberDetailsProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * //컨트롤러에서 Member 사용 시 @AuthenticationPrincipal 어노테이션으로 MemberPrincipal을 불러와 사용
     */

    private final JwtTokenGenerator jwtTokenGenerator;
    private final MemberDetailsProvider memberDetailsProvider;
    private final ManagerDetailsProvider managerDetailsProvider;
    private final AdminDetailsProvider adminDetailsProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        //헤더에서 토큰 값을 읽어오는 과정
        String accessToken = request.getHeader("accessToken");//
        if (accessToken != null) {
            Authentication authentication = getEmailPassword(accessToken);

            SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);//필터 종료 후 다음 필터로 진행

    }


    private Authentication getEmailPassword(String token) {
        String email = jwtTokenGenerator.getUserEmail(token);
        MemberRole role = jwtTokenGenerator.getRole(token);
        if (email != null) {
            UserDetails userDetails = getUserDetails(email, role);
            return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
        }
        return null;
    }

    private UserDetails getUserDetails(String email, MemberRole role) {
        if(role == MemberRole.ADMIN) {
            return adminDetailsProvider.loadUserByUsername(email);
        }
        if (role == MemberRole.MANAGER) {
            return managerDetailsProvider.loadUserByUsername(email);
        }
        return memberDetailsProvider.loadUserByUsername(email);
    }
}
