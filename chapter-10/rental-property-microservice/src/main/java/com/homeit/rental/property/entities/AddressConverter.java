package com.homeit.rental.property.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter
public class AddressConverter
    implements AttributeConverter<Address, String> {

    private final ObjectMapper objectMapper =
        new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Address address) {
        try {
            return objectMapper
                .writeValueAsString(address);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException
                ("Error converting Address to JSON string", e);
        }
    }

    @Override
    public Address convertToEntityAttribute(String dbData) {
        try {
           return objectMapper
               .readValue(dbData, Address.class);
        } catch (IOException e) {
            throw new IllegalArgumentException
                ("Error converting JSON string to Address", e);
        }
    }
}