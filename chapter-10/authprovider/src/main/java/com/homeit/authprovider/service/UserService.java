package com.homeit.authprovider.service;

import com.homeit.authprovider.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> createUser(String email, String password, String userType) ;
    Optional<UserDTO> findByEmail(String email) ;
    boolean validateUser(String email, String password);
}
