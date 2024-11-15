package com.rao.common.config;

import com.rao.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config {

//    @Value("${JWT_SECRET_KEY}")
//    private String secretKey;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil("jilinUniversityComputerScienceAndTechnology");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
