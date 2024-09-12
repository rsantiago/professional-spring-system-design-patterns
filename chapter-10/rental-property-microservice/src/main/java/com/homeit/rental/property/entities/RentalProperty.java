package com.homeit.rental.property.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "rental_properties")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalProperty {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Landlord id is required")
    @Column(nullable = false)
    private UUID landlordID;

    @NotEmpty(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Lob
    @Convert(converter = AddressConverter.class)
    @Column(columnDefinition = "TEXT")
    private Address address;

    @NotNull(message = "Rent is required")
    @Column(nullable = false)
    private Double rent;

    private Integer score = 0;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}