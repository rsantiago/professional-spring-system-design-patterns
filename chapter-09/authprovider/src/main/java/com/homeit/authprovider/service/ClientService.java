package com.homeit.authprovider.service;

public interface ClientService {
    boolean validateClient(String clientId, String clientSecret);
}
