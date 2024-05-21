package com.final_10aeat.domain.admin.service;

import com.final_10aeat.domain.admin.dto.response.EmailVerificationResponseDto;
import com.final_10aeat.domain.member.entity.MemberRole;
import com.final_10aeat.domain.admin.exception.InvalidVerificationCodeException;
import com.final_10aeat.domain.admin.exception.VerificationCodeExpiredException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Profile({"local", "test"})
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LocalEmailService implements EmailUseCase {

    private final RedisTemplate<String, String> redisTemplate;
    private final Gson gson = new Gson();

    @Override
    public String sendVerificationEmail(String to, MemberRole role, String dong, String ho) {
        String authCode = generateAuthCode();
        log.info("이메일 인증번호 발송");
        log.info("email : {}", to);
        log.info("code : {}", authCode);
        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("email", to);
        userInfo.addProperty("role", role.name());
        userInfo.addProperty("dong", dong);
        userInfo.addProperty("ho", ho);
        saveVerificationCode(to, authCode, userInfo.toString(), 1440);
        return authCode;
    }

    private void saveVerificationCode(String email, String code, String userInfo, long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(email + ":code", code, timeout, TimeUnit.MINUTES);
        ops.set(email + ":info", userInfo, timeout, TimeUnit.MINUTES);
    }

    @Override
    public EmailVerificationResponseDto verifyEmailCode(String email, String code) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedCode = ops.get(email + ":code");
        String userInfoJson = ops.get(email + ":info");

        if (storedCode == null || userInfoJson == null) {
            throw new VerificationCodeExpiredException();
        }
        if (!code.equals(storedCode)) {
            throw new InvalidVerificationCodeException();
        }
        return gson.fromJson(userInfoJson, EmailVerificationResponseDto.class);
    }

    private String generateAuthCode() {
        Random random = new Random();
        return random.ints(8, 0, 36)
            .mapToObj(i -> i < 10 ? String.valueOf(i) : String.valueOf((char) (i - 10 + 'a')))
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
    }
}
