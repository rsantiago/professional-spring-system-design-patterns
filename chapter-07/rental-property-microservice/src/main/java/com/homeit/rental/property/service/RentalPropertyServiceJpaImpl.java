package com.homeit.rental.property.service;

import com.homeit.rental.property.dto.RentalPropertyDTO;
import com.homeit.rental.property.dto.utils.RentalPropertyConverter;
import com.homeit.rental.property.entities.RentalProperty;
import com.homeit.rental.property.repositories.RentalPropertyJpaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Qualifier("jpaRentalPropertyService")
public class RentalPropertyServiceJpaImpl implements RentalPropertyService{

    private final RentalPropertyJpaRepository jpaRepository;

    public RentalPropertyServiceJpaImpl
        (RentalPropertyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<RentalPropertyDTO> getAllProperties() {
        return jpaRepository.findAll().stream().map(
            RentalPropertyConverter::toDTO
        ).toList();
    }

    @Override
    public Page<RentalPropertyDTO> getPagedProperties(int page, int size) {
        final PageRequest pageable = PageRequest.of(page, size);

        return PageableExecutionUtils.getPage(jpaRepository
            .findAll(pageable)
            .getContent().stream()
            .map(RentalPropertyConverter::toDTO)
            .toList(), pageable, jpaRepository::count);
    }

    @Override
    public Optional<RentalPropertyDTO> get(UUID id) {
        return jpaRepository.findById(id)
            .map(RentalPropertyConverter::toDTO);
    }

    @Override
    public RentalPropertyDTO create(RentalPropertyDTO property) {
        return RentalPropertyConverter.toDTO(
            jpaRepository.save(
                RentalPropertyConverter.toEntity(property)));
    }

    @Override
    @Transactional
    public Optional<RentalPropertyDTO> update(UUID id, RentalPropertyDTO updatedProperty) {
        if(jpaRepository.existsById(id)) {
            return Optional.ofNullable(
                RentalPropertyConverter.toDTO(
                    jpaRepository.save(RentalPropertyConverter
                        .toEntity(updatedProperty))));
        }

        return Optional.empty();
    }

    @Override
    public Optional<RentalPropertyDTO> updateSomeFields(UUID id, RentalPropertyDTO partialUpdate) {
        return get(id)
            .map(
                original ->
                    RentalPropertyDTO.builder()
                        .id(id) // the id is the only thing that will never change
                        .address(nullOrEmpty(partialUpdate.address())? original.address() : partialUpdate.address())
                        .name(nullOrEmpty(partialUpdate.name())? original.name() : partialUpdate.name())
                        .rent(nullOrEmpty(partialUpdate.rent())? original.rent() : partialUpdate.rent())
                        .city(nullOrEmpty(partialUpdate.city())? original.city() : partialUpdate.city())
                        .landlordID(nullOrEmpty(partialUpdate.landlordID())? original.landlordID() : partialUpdate.landlordID())
                        .country((nullOrEmpty(partialUpdate.country())? original.country() : partialUpdate.country()))
                        .zipCode((nullOrEmpty(partialUpdate.zipCode())? original.zipCode() : partialUpdate.zipCode()))
                    .build())
            .map(RentalPropertyConverter::toEntity)
            .map(jpaRepository::save)
            .map(RentalPropertyConverter::toDTO);
    }

    private boolean nullOrEmpty(Object address) {
        if(address == null) return true;
        if(address instanceof String) return ((String)address).isEmpty();
        return false;
    }

    @Override
    public Optional<RentalPropertyDTO> delete(UUID id) {
        Optional<RentalPropertyDTO> found = get(id);
        jpaRepository.deleteById(id);
        return found;
    }

    @Override
    public List<RentalPropertyDTO> search(
            String name, String address,
            String city, String country, String zipCode) {
        throw new UnsupportedOperationException(
            "This service implementation does not support searches." +
            " Please, use another service implementation instead.");
    }
}
