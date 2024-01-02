package com.example.reactive.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Service

@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private static final String PREFIX = "Bearer ";
    private static final String ROLES_KEY = "roles";
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        Assert.notNull(userDetails, "user details cannot be null");

        ClaimsBuilder claimsBuilder = Jwts.claims()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getTokenLiveTime()))
                .add(extraClaims);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        if (!authorities.isEmpty()) {
            claimsBuilder.add(ROLES_KEY, authorities.stream()
                    .map(GrantedAuthority::getAuthority).collect(joining(",")));
        }

        return Jwts.builder().claims(claimsBuilder.build())
                .signWith(secretKey, Jwts.SIG.HS256).compact();
    }
}
