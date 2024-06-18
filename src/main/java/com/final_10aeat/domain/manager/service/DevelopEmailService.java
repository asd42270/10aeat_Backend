package com.final_10aeat.domain.manager.service;

import com.final_10aeat.domain.manager.dto.response.VerifyEmailResponseDto;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.manager.exception.EmailSendingException;
import com.final_10aeat.domain.manager.exception.EmailTemplateLoadException;
import com.final_10aeat.domain.manager.exception.InvalidVerificationCodeException;
import com.final_10aeat.domain.manager.exception.ExpiredVerificationCodeException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("develop")
@RequiredArgsConstructor
@Transactional
public class DevelopEmailService implements EmailUseCase {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final Gson gson = new Gson();
    private static final String TEST_ID_EMAIL = "fasttime123@naver.com";

    @Override
    public String sendVerificationEmail(String to, MemberRole role, String dong, String ho,
        Long officeId)
        throws MessagingException, UnsupportedEncodingException {
        String authCode = generateAuthCode();
        MimeMessage message = createMessage(to, authCode);
        try {
            javaMailSender.send(message);
            JsonObject userInfo = new JsonObject();
            userInfo.addProperty("email", to);
            userInfo.addProperty("role", role.name());
            userInfo.addProperty("dong", dong);
            userInfo.addProperty("ho", ho);
            userInfo.addProperty("officeId", officeId);
            saveVerificationCode(to, authCode, userInfo.toString(), 1440);
            return authCode;
        } catch (MailException ex) {
            throw new EmailSendingException();
        }
    }

    private void saveVerificationCode(String email, String code, String userInfo, long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(email + ":code", code, timeout, TimeUnit.MINUTES);
        ops.set(email + ":info", userInfo, timeout, TimeUnit.MINUTES);
    }

    @Override
    public VerifyEmailResponseDto verifyEmailCode(String email, String code) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedCode = ops.get(email + ":code");
        String userInfoJson = ops.get(email + ":info");

        if (storedCode == null || userInfoJson == null) {
            throw new ExpiredVerificationCodeException();
        }
        if (!code.equals(storedCode)) {
            throw new InvalidVerificationCodeException();
        }
        return gson.fromJson(userInfoJson, VerifyEmailResponseDto.class);
    }

    private String generateAuthCode() {
        Random random = new Random();
        return random.ints(8, 0, 36)
            .mapToObj(i -> i < 10 ? String.valueOf(i) : String.valueOf((char) (i - 10 + 'a')))
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
    }

    private MimeMessage createMessage(String to, String authCode)
        throws MessagingException, UnsupportedEncodingException {
        String setFrom = TEST_ID_EMAIL;
        String title = "회원가입 인증 번호";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true,
            StandardCharsets.UTF_8.name());

        String emailTemplate = loadEmailTemplate("email_template.html");

        emailTemplate = emailTemplate.replace("{{authCode}}", authCode);

        helper.setSubject(title);
        helper.setFrom(new InternetAddress(setFrom, "10aeat", StandardCharsets.UTF_8.name()));
        helper.setTo(to);
        helper.setText(emailTemplate, true);

        return message;
    }

    private String loadEmailTemplate(String templateName) {
        try {
            Resource resource = new ClassPathResource("templates/" + templateName);
            InputStream inputStream = resource.getInputStream();
            byte[] templateBytes = inputStream.readAllBytes();
            return new String(templateBytes, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new EmailTemplateLoadException();
        }
    }
}
