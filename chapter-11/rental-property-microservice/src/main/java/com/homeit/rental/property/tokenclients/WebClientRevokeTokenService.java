package com.homeit.rental.property.tokenclients;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientRevokeTokenService {

    private final WebClient webClient;

    public WebClientRevokeTokenService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://your-revoked-token-service-url").build();
    }

    public Mono<Boolean> isTokenRevoked(String token) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/revoked-tokens/check")
                        .queryParam("token", token)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false); // Handle errors by returning a default value
    }
}