package com.homeit.authprovider.service;

import org.springframework.stereotype.Service;

@Service
public class ScopeServiceImpl implements ScopeService{
    @Override
    public String findScope(String userType) {
        if("tenant".equals(userType)) {
            return "rental_properties:read";
        }
        if("landlord".equals(userType)) {
            return "rental_properties:read rental_properties:write";
        }

        return null;
    }
}
