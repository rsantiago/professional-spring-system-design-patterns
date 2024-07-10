package com.homeit.rental.property.service;

import com.homeit.rental.property.dto.RentalPropertyDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalPropertyService {
    List<RentalPropertyDTO> getAllProperties();

    Page<RentalPropertyDTO> getPagedProperties(int page, int size);

    Optional<RentalPropertyDTO> get(UUID id);

    RentalPropertyDTO create(RentalPropertyDTO property);

    Optional<RentalPropertyDTO>
        update(UUID id, RentalPropertyDTO updatedProperty);

    Optional<RentalPropertyDTO>
        updateSomeFields(UUID id, RentalPropertyDTO partialUpdate);

    Optional<RentalPropertyDTO> delete(UUID id, String authorizedUser);

    List<RentalPropertyDTO> search(String name,
       String address, String city, String country, String zipCode);
}
