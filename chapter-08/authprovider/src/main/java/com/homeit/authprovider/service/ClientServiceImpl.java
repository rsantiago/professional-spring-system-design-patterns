package com.homeit.authprovider.service;

import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService{

    private final String clientId = "4ca8f880-0bee-4c24-88ce-3402fe7e37f0";
    private final String clientSecret = "b08cee2b-e79f-472b-a0b9-b210465c8bf3";

    @Override
    public boolean validateClient(String clientId, String clientSecret) {
        return this.clientId.equals(clientId)
            && this.clientSecret.equals(clientSecret);
    }
}
