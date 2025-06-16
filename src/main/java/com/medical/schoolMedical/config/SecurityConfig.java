package com.medical.schoolMedical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    Biến để khai báo các cái endpoint muốn public
    private final String[] PUBLIC_GET_ENDPOINTS = {
        "/register"
    };
    private final String[] PUBLIC_POST_ENDPOINTS = {
            "/register"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
//                Mặc định security sẽ bật f
                .csrf(httpCsrf -> httpCsrf.disable())
//                lamda function nên đặt tên dễ hiểu
                .authorizeHttpRequests(request -> request
//                        Yêu cầu 2 params là method và endpoint
//                          endpoint nào cho phép truy cập thì để permitAlL
//                                cái param endpoint là list nên có thể nhập nhiều đường dẫn
//                                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
//                                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
//                                còn các endpoint khác vẫn phải authenticate
                        .anyRequest().permitAll()
                );
        return httpSecurity.build();
    }
}
