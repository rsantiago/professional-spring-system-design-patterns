package com.homeit.rental.property.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeit.rental.property.dto.RentalPropertyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class RentalPropertyRowMapper implements RowMapper<RentalPropertyDTO> {

    private ObjectMapper objectMapper;

    public RentalPropertyRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public RentalPropertyDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        String address = null;
        try {
            address = objectMapper.readValue(rs.getString("address"), String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error while trying to read a string", e);
        }

        return RentalPropertyDTO.builder()
            .id(UUID.fromString(rs.getString("id")))
            .landlordID(UUID.fromString(rs.getString("landlordid")))
            .name(rs.getString("name"))
            .address(address)
            .rent(rs.getDouble("rent"))
            .build();
    }
}