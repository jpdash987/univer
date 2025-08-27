package com.univer.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class JwtBeans {

    @Value("${JWT_SECRET:dev-secret-change-me}")
    private String hmacSecret;

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter(); // reads "scope"/"scp"
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
}