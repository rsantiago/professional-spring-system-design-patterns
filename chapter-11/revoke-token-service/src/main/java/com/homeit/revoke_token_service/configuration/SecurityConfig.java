package com.homeit.revoke_token_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String APP_ID = "myAppId";
    private static final String APP_SECRET = "mySecret";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(spec -> spec.anyExchange().authenticated())
                .httpBasic(authConfigurer -> authConfigurer.authenticationManager(authentication -> {
                    String principal = authentication.getName();
                    String credentials = authentication.getCredentials().toString();
                    if (APP_ID.equals(principal) && APP_SECRET.equals(credentials)) {
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                principal, credentials, Collections.emptyList());
                        return Mono.just(auth);
                    } else {
                        return Mono.empty(); // Returning an empty Mono if authentication fails
                    }
                }))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }
}