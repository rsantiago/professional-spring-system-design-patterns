package com.homeit.authprovider.service;

import com.homeit.authprovider.dto.UserConverter;
import com.homeit.authprovider.dto.UserDTO;
import com.homeit.authprovider.entities.UserEntity;
import com.homeit.authprovider.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserDTO> createUser(String email, String password, String userType) {
        if(!userRepository.findByEmail(email).isEmpty()) {
            return Optional.empty(); // email already used
        }

        return Optional.of(userRepository.save(new UserEntity(UUID.randomUUID(),
            email, passwordEncoder.encode(password),userType)))
                .map(UserConverter::fromUserEntity);
    }

    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(
        UserConverter::fromUserEntity);
    }

    @Override
    public boolean validateUser(String email, String password) {
        return userRepository.findByEmail(email)
            .map(user -> passwordEncoder.matches(password,user.getPassword()))
            .orElse(false);
    }
}