package com.sheguard.userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for generating and validating jwt token
 */
@Slf4j
@Service
public class JwtProvider {

    public final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET_KEY.getBytes());

    /**
     * generate jwt token based on the authentication object received
     * token will be generated when user will log in
     */
    public String generateJwtToken(Authentication authentication) {
        log.info("Generating jwt token for user {}", authentication.getName());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = getRoles(authorities);

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86_40_00_00))
                .claim("email", authentication.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();

        log.info("Generated JWT Token {}", jwt);
        return jwt;
    }

    // generate email from jwt
    public String generateEmailFromJwt(String jwt) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

        return String.valueOf(claims.get("email"));
    }

    // convert a list of user roles (Spring format) into a single comma-separated string.
    private String getRoles(Collection<? extends GrantedAuthority> authorities) {
        Set<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return String.join(",", roles);
    }
}
