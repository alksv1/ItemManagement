package com.rao.userservice.config;

import com.rao.userservice.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class BeanConfig implements EnvironmentAware {

    private String secretKey;

    @Bean
    public AESUtil aesUtil() throws Exception {
        return new AESUtil(secretKey, 300000);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void setEnvironment(Environment env) {
        secretKey = env.getProperty("AES_SECRET_KEY");
        log.info(secretKey);
    }
}
