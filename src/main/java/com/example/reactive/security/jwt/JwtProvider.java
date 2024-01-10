package com.example.reactive.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    public static final String PREFIX = "Bearer ";
    private static final String ROLES_KEY = "roles";
    private static final String AUTH_CANNOT_BE_NULL = "authentication cannot be null";
    private static final String CLAIMS_CANNOT_BE_NULL = "claims cannot be null";

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public String generateToken(Authentication authentication) {
        return generateToken(authentication.getName(), authentication.getAuthorities(), Map.of());
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(), userDetails.getAuthorities(), Map.of());
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities, Map<String, ?> extraClaims) {
        Assert.notNull(username, AUTH_CANNOT_BE_NULL);
        Assert.notNull(extraClaims, CLAIMS_CANNOT_BE_NULL);

        ClaimsBuilder claimsBuilder = Jwts.claims()
                .subject(username)
                .issuer("spring.webflux.test")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getTokenLiveTime()))
                .add(extraClaims);
        if (!authorities.isEmpty()) {
            claimsBuilder.add(ROLES_KEY, authorities.stream()
                    .map(GrantedAuthority::getAuthority).collect(joining(",")));
        }

        return Jwts.builder().claims(claimsBuilder.build())
                .signWith(secretKey, Jwts.SIG.HS256).compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public Jws<Claims> getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }


    public boolean isTokenValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(this.secretKey)
                    .build().parseSignedClaims(token);
            // parseClaimsJws will check expiration date. No need do here.
            log.debug("expiration date: {}", claims.getPayload().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT token: {}", e.getMessage());
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}
