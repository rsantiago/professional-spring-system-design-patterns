package com.homeit.rental.property.service;

import com.homeit.rental.property.dto.RentalPropertyDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalPropertyService {
    List<RentalPropertyDTO> getAllProperties();

    Optional<RentalPropertyDTO> get(UUID id);

    RentalPropertyDTO create(RentalPropertyDTO property);

    Optional<RentalPropertyDTO>
        update(UUID id, RentalPropertyDTO updatedProperty);

    Optional<RentalPropertyDTO>
        updateSomeFields(UUID id, RentalPropertyDTO partialUpdate);

    Optional<RentalPropertyDTO> delete(UUID id);

    List<RentalPropertyDTO> search(String name,
       String address, String city, String country, String zipCode);
}
