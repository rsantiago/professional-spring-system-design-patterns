package com.homeit.revoke_token_service.services;
import com.homeit.revoke_token_service.entities.RevokedToken;
import com.homeit.revoke_token_service.repository.RevokedTokenRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RevokeTokenService {

    private final RevokedTokenRepository repository;

    public RevokeTokenService(RevokedTokenRepository repository) {
        this.repository = repository;
    }

    public Mono<RevokedToken> revokeToken(String token) {
        return repository.save(new RevokedToken(token));
    }

    public Mono<RevokedToken> getRevokedToken(String token) {
        return repository.getByRevoked(token);
    }
}