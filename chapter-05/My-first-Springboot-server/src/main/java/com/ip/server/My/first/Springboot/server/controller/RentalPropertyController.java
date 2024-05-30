package com.ip.server.My.first.Springboot.server.controller;

import com.ip.server.My.first.Springboot.server.dto.RentalPropertyDTO;
import com.ip.server.My.first.Springboot.server.service.RentalPropertyService;
import jakarta.validation.Valid;
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

    private final RentalPropertyService rentalPropertyService;

    public RentalPropertyController(
            RentalPropertyService rentalPropertyService) {
        this.rentalPropertyService = rentalPropertyService;
    }

    @GetMapping(
        value = "/{id}",
        produces = "application/json")
    public ResponseEntity<RentalPropertyDTO> getPropertyById(
        @PathVariable UUID id) {
        return rentalPropertyService.get(id)
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
                = rentalPropertyService.create(property);

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

        return rentalPropertyService
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

        return rentalPropertyService
            .updateSomeFields(id, partialUpdate)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        return rentalPropertyService
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

        return ResponseEntity.ok(rentalPropertyService.search(
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
    public ResponseEntity<List<RentalPropertyDTO>> getAllProperties() {
        return ResponseEntity.ok()
                .body(rentalPropertyService.getAllProperties());
    }


    @GetMapping(value = "/error")
    public ResponseEntity<List<RentalPropertyDTO>> runtimeExceptionSample() {
        throw new RuntimeException
                ("This was a sample unhandled runtime exception");
    }
}