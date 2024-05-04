package com.final_10aeat.global.security.jwt;


import com.final_10aeat.global.security.principal.MemberDetailsProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     //@AuthenticationPrincipal 컨트롤러에서 Member 사용 시 이 어노테이션으로 바로 파싱 가능
     */

    private final JwtTokenGenerator jwtTokenGenerator;
    private final MemberDetailsProvider memberDetailsProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        //헤더에서 토큰 값을 읽어오는 과정
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);//기본적으로 포함되는 것
        if(accessToken!=null){
            Authentication authentication = getEmailPassword(accessToken);

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response); //이 필터 끝났으니 다음 필터로 진행해라

    }


    private Authentication getEmailPassword(String token) {
        String email = jwtTokenGenerator.getUserEmail(token);
        if (email != null){
            UserDetails userDetails = memberDetailsProvider.loadUserByUsername(email);
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    authorities
            );
        }
        return null;
    }
}
