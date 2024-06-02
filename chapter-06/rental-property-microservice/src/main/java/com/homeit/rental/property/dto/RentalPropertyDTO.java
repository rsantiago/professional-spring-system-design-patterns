package com.homeit.rental.property.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RentalPropertyDTO(
    UUID id,

    @NotNull(message = "Landlord id is required")
    UUID landlordID,

    @NotEmpty(message = "Name is required")
    String name,

    @NotEmpty(message = "Address is required")
    String address,

    @NotEmpty(message = "City is required")
    String city,

    @NotEmpty(message = "Country is required")
    String country,

    @NotEmpty(message = "Zip code is required")
    String zipCode,

    @NotNull(message = "Rent is required")
    Double rent
) { }
