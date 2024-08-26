package com.homeit.revoke_token_service.configuration;

import com.homeit.revoke_token_service.RevokeTokenServiceApplication;
import com.homeit.revoke_token_service.controller.RevokeTokenHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(RevokeTokenHandler handler) {
        return RouterFunctions
            .route(RequestPredicates.POST("/api/revoke-tokens"), handler::revokeToken)
            .andRoute(RequestPredicates.GET("/api/revoke-tokens"), handler::isTokenRevoked);
    }
}