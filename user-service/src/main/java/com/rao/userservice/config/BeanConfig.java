package com.rao.userservice.config;

import com.rao.userservice.util.AESUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public AESUtil aesUtil() throws Exception {
        return new AESUtil("1234567890123456", 300000);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
