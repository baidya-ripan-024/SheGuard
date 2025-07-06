package com.sheguard.userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Geeting the jwt key from the header
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if(jwt == null || jwt.isEmpty()) {
            log.info("No jwt token found in the request header.");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Validating jwt token: {}", jwt);

        if(jwt.startsWith("Bearer ") && !jwt.isEmpty()) {
            jwt = jwt.substring(7);
        }

        try {
            log.info("Parsing jwt token.");

            // Retrives the secret key used to singin jwt and
            // Converts the secret into a SecretKey object using hmacShaKeyFor.
            SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET_KEY.getBytes());

            // parse jwt token and extracting the claims
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt).getBody();

            // extract email and authorities from token claims
            String email = String.valueOf(claims.get("email"));
            String authorities = String.valueOf(claims.get("authorities"));

            /**
             * Converts the comma-separated authorities string (like "ROLE_USER,ROLE_ADMIN") into a
             * list of GrantedAuthority objects.
             *
             * Required by Spring Security to represent user roles/permissions.
             */
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            // Create an authentication object
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authentication object in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", jwt);
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", jwt);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.warn("JWT token is malformed: {}", jwt);
        } catch (io.jsonwebtoken.SignatureException e) {
            log.warn("JWT signature is invalid: {}", jwt);
        } catch (IllegalArgumentException e) {
            log.warn("JWT token is empty or null");
        } catch (Exception e) {
            log.error("Unexpected error while parsing JWT token: {}", jwt, e);
        }

        // move to the next filter
        filterChain.doFilter(request, response);
    }
}
