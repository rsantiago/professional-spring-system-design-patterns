package com.homeit.rental.property.service;

import com.homeit.rental.property.dto.RentalPropertyDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Qualifier("jdbcRentalPropertyService")
public class RentalPropertyServiceJdbcImpl implements RentalPropertyService{

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RentalPropertyRowMapper rentalPropertyRowMapper;

    public RentalPropertyServiceJdbcImpl(NamedParameterJdbcTemplate jdbcTemplate, RentalPropertyRowMapper rentalPropertyRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rentalPropertyRowMapper = rentalPropertyRowMapper;
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
        StringBuilder sql = new StringBuilder("SELECT * FROM rental_properties WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (StringUtils.hasText(name)) {
            sql.append(" AND name LIKE :name");
            params.addValue("name", "%" + name + "%");
        }
        if (StringUtils.hasText(address)) {
            sql.append(" AND address LIKE :address");
            params.addValue("address", "%" + address + "%");
        }
        if (StringUtils.hasText(city)) {
            sql.append(" AND address LIKE :city");
            params.addValue("city", "%" + city + "%");
        }
        if (StringUtils.hasText(country)) {
            sql.append(" AND address LIKE :country");
            params.addValue("country", "%" + country + "%");
        }
        if (StringUtils.hasText(zipCode)) {
            sql.append(" AND address LIKE :zipCode");
            params.addValue("zipCode", "%" + zipCode + "%");
        }

        return jdbcTemplate.query(sql.toString(), params, rentalPropertyRowMapper);
    }
}