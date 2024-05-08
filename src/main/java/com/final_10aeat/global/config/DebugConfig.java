package com.final_10aeat.global.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DebugConfig {

    @Value("${spring.datasource.url}")
    String url;

    @PostConstruct
    public void okay(){
        log.info("url log:{} ", url);
    }
}
