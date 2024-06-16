package com.homeit.rental.property.controller;

import com.homeit.rental.property.dto.RentalPropertyDTO;
import com.homeit.rental.property.service.RentalPropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/rental-properties")
@Validated
public class RentalPropertyController {

    private final RentalPropertyService jpaRentalPropertyService;
    private final RentalPropertyService jdbcRentalPropertyService;
    private final RentalPropertyService entityManagerRentalPropertyService;

    public RentalPropertyController(
            @Qualifier("jpaRentalPropertyService") RentalPropertyService jpaRentalPropertyService,
            @Qualifier("jdbcRentalPropertyService") RentalPropertyService jdbcRentalPropertyService,
            @Qualifier("entityManagerRentalPropertyService") RentalPropertyService entityManagerRentalPropertyService) {
        this.jpaRentalPropertyService = jpaRentalPropertyService;
        this.jdbcRentalPropertyService = jdbcRentalPropertyService;
        this.entityManagerRentalPropertyService = entityManagerRentalPropertyService;
    }


    @GetMapping(
        value = "/{id}",
        produces = "application/json")
    public ResponseEntity<RentalPropertyDTO> getPropertyById(
        @PathVariable UUID id) {
        return jpaRentalPropertyService.get(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null));
    }

    @PostMapping(
        consumes = "application/json",
        produces = "application/json")
    public ResponseEntity<RentalPropertyDTO> createProperty(
        @Valid @RequestBody RentalPropertyDTO property) {

        RentalPropertyDTO createdRentalProperty
                = jpaRentalPropertyService.create(property);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createdRentalProperty);
    }

    @PutMapping(
        value = "/{id}",
        consumes = "application/json",
        produces = "application/json")
    public ResponseEntity<RentalPropertyDTO> updateProperty(
        @PathVariable UUID id,
        @Valid @RequestBody RentalPropertyDTO updatedProperty) {

        return jpaRentalPropertyService
            .update(id, updatedProperty)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null));
    }

    @PatchMapping(
        value = "/{id}",
        consumes = "application/json",
        produces = "application/json")
    public ResponseEntity<RentalPropertyDTO> patchProperty(
        @PathVariable UUID id,
        @RequestBody RentalPropertyDTO partialUpdate) {

        return jpaRentalPropertyService
            .updateSomeFields(id, partialUpdate)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        return entityManagerRentalPropertyService
            .delete(id)
            .map(opt ->
                ResponseEntity.noContent()
                    .<Void>build())
            .orElse(ResponseEntity
                .status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping(
        value = "/search",
        produces = "application/json")
    public ResponseEntity<List<RentalPropertyDTO>> searchProperties(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String address,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String zipCode) {

        return ResponseEntity.ok(jdbcRentalPropertyService.search(
            name, address, city,country,zipCode));
    }

    @GetMapping(
        value = "/headers",
        produces = "application/json")
    public ResponseEntity<String> getHeaderInfo(
            @RequestHeader("User-Agent") String userAgent) {
        return ResponseEntity.ok("User-Agent: " + userAgent);
    }


    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<RentalPropertyDTO>> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok()
                .body(jpaRentalPropertyService.getPagedProperties(page, size));
    }

    @GetMapping(value = "/error")
    public ResponseEntity<List<RentalPropertyDTO>> runtimeExceptionSample() {
        throw new RuntimeException
            ("This was a sample unhandled runtime exception");
    }

    @GetMapping("/thread-model")
    public String getThreadName() {
        return Thread.currentThread().toString();
    }
}