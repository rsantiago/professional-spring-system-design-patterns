package com.homeit.rental.property.dto.utils;

import com.homeit.rental.property.dto.RentalPropertyDTO;
import com.homeit.rental.property.entities.Address;
import com.homeit.rental.property.entities.RentalProperty;

public class RentalPropertyConverter {
    public static RentalProperty toEntity(RentalPropertyDTO dto) {
        return RentalProperty.builder()
            .rent(dto.rent())
            .id(dto.id())
            .name(dto.name())
            .landlordID(dto.landlordID())
            .address(Address.builder()
                .zip(dto.zipCode())
                .city(dto.city())
                .streetAddress(dto.address())
                .country(dto.country())
                .build())
            .score(0)
            .build();
    }

    public static RentalPropertyDTO toDTO(RentalProperty entity) {
        return RentalPropertyDTO.builder()
            .address(entity.getAddress().streetAddress())
            .name(entity.getName())
            .rent(entity.getRent())
            .city(entity.getAddress().city())
            .landlordID(entity.getLandlordID())
            .id(entity.getId())
            .country(entity.getAddress().country())
            .score(entity.getScore())
            .zipCode(entity.getAddress().zip())
            .build();
    }
}
