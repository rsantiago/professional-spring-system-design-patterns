package com.homeit.rental.property.controller;

import com.homeit.rental.property.controller.utils.RentalPropertyHyperMediaUtils;
import com.homeit.rental.property.dto.descriptors.PropertiesCollectionDescriptor;
import com.homeit.rental.property.dto.descriptors.RentalPropertyDescriptor;
import com.homeit.rental.property.service.RentalPropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v2/rental-properties")
@Validated
public class RentalPropertyControllerV2 {

    private final RentalPropertyService rentalPropertyService;
    private final RentalPropertyHyperMediaUtils rentalPropertyHyperMediaUtils;

    public RentalPropertyControllerV2(
            RentalPropertyService rentalPropertyService,
            RentalPropertyHyperMediaUtils rentalPropertyHyperMediaUtils) {
        this.rentalPropertyService = rentalPropertyService;
        this.rentalPropertyHyperMediaUtils = rentalPropertyHyperMediaUtils;
    }

    @GetMapping(
        value = "/{id}",
        produces = "application/json")
    public ResponseEntity<RentalPropertyDescriptor> getPropertyById(
        @PathVariable UUID id) {
        return rentalPropertyService.get(id)
            .map(rentalPropertyHyperMediaUtils::describeRentalProperty)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<PropertiesCollectionDescriptor> getAllProperties() {
        return Optional.ofNullable(
                rentalPropertyHyperMediaUtils
                    .describeRentalPropertyCollection(
                        rentalPropertyService.getAllProperties()))
            .map( describedCollection ->
                ResponseEntity.ok().body(describedCollection))
            .orElse(ResponseEntity.noContent().build());
    }

}