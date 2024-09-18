package com.homeit.authprovider.controller;

import com.homeit.authprovider.dto.TokenRequest;
import com.homeit.authprovider.dto.TokenResponse;
import com.homeit.authprovider.dto.UserDTO;
import com.homeit.authprovider.service.ClientService;
import com.homeit.authprovider.service.JWTService;
import com.homeit.authprovider.service.ScopeService;
import com.homeit.authprovider.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ClientService clientService;
    private final JWTService jwtService;
    private final ScopeService scopeService;

    public UserController(UserService userService, ClientService clientService, JWTService jwtService, ScopeService scopeService) {
        this.userService = userService;
        this.clientService = clientService;
        this.jwtService = jwtService;
        this.scopeService = scopeService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDto) {
        return userService.createUser(userDto.email(), userDto.password(), userDto.user_type())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping(
            value = "/token",
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest tokenRequest) {

        if(!"password".equals(tokenRequest.grant_type())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!clientService.validateClient(tokenRequest.client_id(),tokenRequest.client_secret())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!userService.validateUser(tokenRequest.username(), tokenRequest.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDTO foundUser = userService.findByEmail(tokenRequest.username()).get();

        return ResponseEntity.ok(
            jwtService.getJWTToken(tokenRequest,
                scopeService.findScope(
                    foundUser.user_type()),
                    foundUser.id()));
    }

}