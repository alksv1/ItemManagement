package com.rao.common.config;

import com.rao.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class Config implements EnvironmentAware {

    private String secretKey;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secretKey);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void setEnvironment(Environment env) {
        secretKey = env.getProperty("JWT_SECRET_KEY");
        log.info("JWT_SECRET_KEY:{}", secretKey);
    }
}
