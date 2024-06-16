package com.homeit.rental.property.service;

import com.homeit.rental.property.dto.RentalPropertyDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Qualifier("jdbcRentalPropertyService")
public class RentalPropertyServiceJdbcImpl implements RentalPropertyService{

    private final JdbcTemplate jdbcTemplate;

    public RentalPropertyServiceJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RentalPropertyDTO> getAllProperties() {
        throw new UnsupportedOperationException("Not implemented, please use the RentalPropertyJpaImpl bean");
    }

    @Override
    public Page<RentalPropertyDTO> getPagedProperties(int page, int size) {
        throw new UnsupportedOperationException("Not implemented, please use the RentalPropertyJpaImpl bean");
    }

    @Override
    public Optional<RentalPropertyDTO> get(UUID id) {
        throw new UnsupportedOperationException("Not implemented, please use the RentalPropertyJpaImpl bean");
    }

    @Override
    public RentalPropertyDTO create(RentalPropertyDTO property) {
        throw new UnsupportedOperationException("Not implemented, please use the RentalPropertyJpaImpl bean");
    }

    @Override
    public Optional<RentalPropertyDTO> update(UUID id, RentalPropertyDTO updatedProperty) {
        throw new UnsupportedOperationException("Not implemented, please use the RentalPropertyJpaImpl bean");
    }

    @Override
    public Optional<RentalPropertyDTO> updateSomeFields(UUID id, RentalPropertyDTO partialUpdate) {
        throw new UnsupportedOperationException("Not implemented, please use the RentalPropertyJpaImpl bean");
    }

    @Override
    public Optional<RentalPropertyDTO> delete(UUID id) {
        throw new UnsupportedOperationException("Not implemented, please use the RentalPropertyJpaImpl bean");
    }

    @Override
    public List<RentalPropertyDTO> search(String name, String address, String city, String country, String zipCode) {
        String sql = "SELECT * FROM rental_properties WHERE " +
                "name LIKE ? AND " +
                "address LIKE ? AND " +
                "address->>'city' LIKE ? AND " +
                "address->>'country' LIKE ? AND " +
                "address->>'zip' LIKE ?";

        return jdbcTemplate.query(sql, new RentalPropertyRowMapper(),
                "%" + name + "%",
                "%" + address + "%",
                "%" + city + "%",
                "%" + country + "%",
                "%" + zipCode + "%");
    }
}