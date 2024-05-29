package com.ip.server.My.first.Springboot.server.service;

import com.ip.server.My.first.Springboot.server.dto.RentalPropertyDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface RentalPropertyService {
    List<RentalPropertyDTO> getAllProperties();

    Optional<RentalPropertyDTO> get(UUID id);

    RentalPropertyDTO create(RentalPropertyDTO property);

    Optional<RentalPropertyDTO> update(UUID id, RentalPropertyDTO updatedProperty);

    Optional<RentalPropertyDTO> updateSomeFields(UUID id, RentalPropertyDTO partialUpdate);

    Optional<RentalPropertyDTO> delete(UUID id);

    List<RentalPropertyDTO> search(String name, String address, String city, String country, String zipCode);
}
