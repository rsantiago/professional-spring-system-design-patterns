package com.homeit.rental.property.service;

import com.homeit.rental.property.dto.RentalPropertyDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RentalPropertyRowMapper implements RowMapper<RentalPropertyDTO> {

    @Override
    public RentalPropertyDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RentalPropertyDTO.builder()
            .id(UUID.fromString(rs.getString("id")))
            .landlordID(UUID.fromString(rs.getString("landlord_id")))
            .name(rs.getString("name"))
            .address(rs.getString("address"))
            .rent(rs.getDouble("rent"))
            .build();
    }
}