package com.ip.server.My.first.Springboot.server.service;

import com.ip.server.My.first.Springboot.server.dto.RentalPropertyDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RentalPropertyServiceImpl implements RentalPropertyService{
    private final Map<UUID, RentalPropertyDTO>
            rentalProperties = new HashMap<>();

    @Override
    public List<RentalPropertyDTO> getAllProperties() {
        return List.copyOf(rentalProperties.values());
    }

    @Override
    public Optional<RentalPropertyDTO> get(UUID id) {
        return Optional.ofNullable(rentalProperties.get(id));
    }

    @Override
    public RentalPropertyDTO create(RentalPropertyDTO property) {
        UUID id = UUID.randomUUID();
        RentalPropertyDTO newProperty =
                new RentalPropertyDTO(
                        id, property.name(), property.address(),
                        property.city(), property.country(),
                        property.zipCode(), property.rent());

        rentalProperties.put(id, newProperty);

        return newProperty;
    }

    @Override
    public Optional<RentalPropertyDTO> update(
            UUID id,
            RentalPropertyDTO updatedProperty) {
        return Optional.ofNullable(
                rentalProperties.computeIfPresent(
                    id,
                    (foundId, oldProperty) -> new RentalPropertyDTO(
                        id,
                        updatedProperty.name(),
                        updatedProperty.address(),
                        updatedProperty.city(),
                        updatedProperty.country(),
                        updatedProperty.zipCode(),
                        updatedProperty.rent())));
    }

    @Override
    public Optional<RentalPropertyDTO> updateSomeFields(RentalPropertyDTO partialUpdate) {
        return Optional.ofNullable(
                rentalProperties.computeIfPresent(partialUpdate.id(), (id, existingProperty) -> {
            String newName = partialUpdate.name() != null ? partialUpdate.name() : existingProperty.name();
            String newAddress = partialUpdate.address() != null ? partialUpdate.address() : existingProperty.address();
            String newCity = partialUpdate.city() != null ? partialUpdate.city() : existingProperty.city();
            String newCountry = partialUpdate.country() != null ? partialUpdate.country() : existingProperty.country();
            String newZipCode = partialUpdate.zipCode() != null ? partialUpdate.zipCode() : existingProperty.zipCode();
            Double newRent = partialUpdate.rent() != null ? partialUpdate.rent() : existingProperty.rent();

            return new RentalPropertyDTO(partialUpdate.id(), newName, newAddress, newCity, newCountry, newZipCode, newRent);
        }));
    }

    @Override
    public Optional<RentalPropertyDTO> delete(UUID id) {
        return Optional.ofNullable(rentalProperties.remove(id));
    }

    @Override
    public List<RentalPropertyDTO> search(String name, String address, String city, String country, String zipCode) {
        return rentalProperties.values().stream()
                .filter(p -> (name == null || p.name().contains(name)) &&
                        (address == null || p.address().contains(address)) &&
                        (city == null || p.city().contains(city)) &&
                        (country == null || p.country().contains(country)) &&
                        (zipCode == null || p.zipCode().contains(zipCode)))
                .collect(Collectors.toList());
    }
}