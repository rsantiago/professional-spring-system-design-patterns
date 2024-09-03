package com.homeit.rental.property.tokenclients;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class RestTemplateRevokedTokenService {

    private final RestTemplate restTemplate;

    public RestTemplateRevokedTokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isTokenRevoked(String token) {
        String url = "http://localhost:8082/api/revoke-tokens?token=" + token;

        // Create headers and add them to the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic bXlBcHBJZDpteVNlY3JldA=="); // Example header

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Use exchange to send the request with headers
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if(response.getStatusCode().value() == 204) {
            return false;
        }

        if(response.getStatusCode().value() == 200) {
            return Objects.requireNonNull(response.getBody()).contains(token);
        }

        return true;
    }
}