package com.example.reactive.security.jwt;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey = "722236765770276c3c21344b6a455f3662447d3b4b7b3e2c2b707d7e3e";
    private long tokenLiveTime = 1800000;
}
