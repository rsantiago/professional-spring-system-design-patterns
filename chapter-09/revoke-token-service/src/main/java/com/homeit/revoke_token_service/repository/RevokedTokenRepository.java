package com.homeit.revoke_token_service.repository;
import com.homeit.revoke_token_service.entities.RevokedToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RevokedTokenRepository extends ReactiveCrudRepository<RevokedToken, String> {
    Mono<RevokedToken> getByRevoked(String token);
}

