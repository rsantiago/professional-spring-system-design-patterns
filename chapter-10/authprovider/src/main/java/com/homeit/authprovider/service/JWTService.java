package com.homeit.authprovider.service;

import com.homeit.authprovider.dto.TokenRequest;
import com.homeit.authprovider.dto.TokenResponse;

public interface JWTService {
    TokenResponse getJWTToken(TokenRequest tokenRequest, String scope, String userId);
}
