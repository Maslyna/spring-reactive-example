package com.example.reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFlux
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        return http
                .authorizeExchange(exchange -> {
                    exchange.anyExchange().permitAll();
                })
                .httpBasic(withDefaults())
                .build();
    }

}
