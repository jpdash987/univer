package com.univer.academic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${JWT_SECRET:dev-secret-change-me}")
    private String hmacSecret;

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();
        return new JwtAuthenticationConverter() {
            {
                setJwtGrantedAuthoritiesConverter(jwt -> {
                    Collection<GrantedAuthority> authorities = new ArrayList<>(scopes.convert(jwt));
                    Object rolesClaim = jwt.getClaims().get("roles");
                    if (rolesClaim instanceof Collection<?> col) {
                        authorities.addAll(col.stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
                    } else if (rolesClaim instanceof String s && !s.isBlank()) {
                        for (String r : s.split("[,\\s]+")) {
                            if (!r.isBlank()) {
                                String role = r.startsWith("ROLE_") ? r : "ROLE_" + r;
                                authorities.add(new SimpleGrantedAuthority(role));
                            }
                        }
                    }
                    return authorities;
                });
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Health endpoints should be accessible without authentication
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                
                // Academic endpoints with role-based access
                // TEACHER can POST/PUT grades and exams
                .requestMatchers(HttpMethod.POST, "/academic/exams").hasRole("TEACHER")
                .requestMatchers(HttpMethod.POST, "/academic/grades").hasRole("TEACHER")
                .requestMatchers(HttpMethod.PUT, "/academic/grades/**").hasRole("TEACHER")
                .requestMatchers(HttpMethod.PUT, "/academic/exams/**").hasRole("TEACHER")
                
                // STUDENT can GET their own grades/transcripts (self-access endpoints)
                .requestMatchers(HttpMethod.GET, "/academic/*/transcript").hasAnyRole("STUDENT", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/academic/exams").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                
                // ADMIN can access all academic endpoints
                .requestMatchers("/academic/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );
            
        return http.build();
    }
}