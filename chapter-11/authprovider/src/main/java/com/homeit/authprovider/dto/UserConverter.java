package com.homeit.authprovider.dto;

import com.homeit.authprovider.entities.UserEntity;

public class UserConverter {
    public static UserDTO fromUserEntity(UserEntity user) {
        return new UserDTO(user.getId().toString(), user.getEmail(), null, user.getUserType());
    }
}
