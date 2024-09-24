package com.homeit.revoke_token_service.controller;

import com.homeit.revoke_token_service.services.RevokeTokenService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RevokeTokenHandler {

    private final RevokeTokenService revokeTokenService;

    public RevokeTokenHandler(RevokeTokenService revokeTokenService) {
        this.revokeTokenService = revokeTokenService;
    }

    public Mono<ServerResponse> revokeToken(ServerRequest request) {
        String token = request.queryParam("token").orElse("");
        return revokeTokenService.revokeToken(token)
                .flatMap(revokedToken -> ServerResponse.status(201)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(revokedToken))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> isTokenRevoked(ServerRequest request) {
        String token = request.queryParam("token").orElse("");
        return revokeTokenService.getRevokedToken(token)
            .flatMap(revokedToken -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(revokedToken.getRevoked()))
            .switchIfEmpty(ServerResponse.noContent().build());
    }
}