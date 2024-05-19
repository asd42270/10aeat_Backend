package com.final_10aeat.controller;

import com.final_10aeat.global.util.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthCheckController {
    private final StringRedisTemplate redisTemplate;

    @GetMapping("/server")
    public ResponseDTO<Void> serverHealthCheck(){
        return ResponseDTO.ok();
    }

    @GetMapping("/redis")
    public ResponseDTO<?> redisHealthCheck(){
        return ResponseDTO.okWithData(redisTemplate.getConnectionFactory().getConnection().ping());
    }
}
