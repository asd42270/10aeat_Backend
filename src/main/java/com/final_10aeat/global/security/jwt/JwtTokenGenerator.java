package com.final_10aeat.global.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenGenerator {

    //TODO:yml 파일이 구체화 된 후에 주입으로 변경
    private SecretKey key;

    //TODO: yml 구체화 후에 변경
    private final String secret = "dkanfjagrpsk";

    public JwtTokenGenerator() {
        this.key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //TODO: yml 파일이 구체화 된 후에 주입으로 변경
    @Value("${jwt.access-token.access-expired}")
    private Long accessExpiredTimeMills = 1000 * 30L;


    public String createJwtToken(String email){
        return Jwts.builder()
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+accessExpiredTimeMills))
                .signWith(key)
                .compact();
    }

    public String getUserEmail(String token) {
        Claims claims = extractClaim(token);
        return claims.get("email", String.class);
    }


    private Claims extractClaim(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload();
    }




}
