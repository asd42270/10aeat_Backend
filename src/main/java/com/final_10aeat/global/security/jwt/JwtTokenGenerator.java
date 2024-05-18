package com.final_10aeat.global.security.jwt;


import com.final_10aeat.domain.member.entity.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenGenerator {

    //TODO:yml 파일이 구체화 된 후에 주입으로 변경
    private final SecretKey key;

    //TODO: yml 구체화 후에 변경
    private final String secret = "dkanfjagrpskasdfasdfgdfgearfewdxdasdaD";

    public JwtTokenGenerator() {
        this.key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //TODO: yml 파일이 구체화 된 후에 주입으로 변경
//    @Value()
    private final Long accessExpiredTimeMills = 1000 * 60 * 30L;


    public String createJwtToken(String email, MemberRole memberRole) {
        return Jwts.builder()
                .claim("email", email)
                .claim("role",memberRole)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiredTimeMills))
                .signWith(key)
                .compact();
    }

    public String getUserEmail(String token) {
        Claims claims = extractClaim(token);
        return claims.get("email", String.class);
    }

    public String getRole(String token) {
        Claims claims = extractClaim(token);
        return claims.get("role", String.class);
    }

    private Claims extractClaim(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
