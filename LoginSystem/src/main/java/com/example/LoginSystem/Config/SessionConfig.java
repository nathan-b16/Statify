package com.example.LoginSystem.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;

@Configuration
@EnableRedisIndexedHttpSession(maxInactiveIntervalInSeconds = 1800) // 30 minutes, matches spring.session.timeout
public class SessionConfig {
}