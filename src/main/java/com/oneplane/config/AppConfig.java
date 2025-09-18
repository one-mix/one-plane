//작성자: 방대혁
package com.oneplane.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

//    작성자: 방대혁
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}