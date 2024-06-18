package com.homeit.rental.property.repositories;

import com.homeit.rental.property.entities.RentalProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RentalPropertyJpaRepository
    extends JpaRepository<RentalProperty, UUID> {

    // Find all properties by landlord ID
    List<RentalProperty> findByLandlordID(UUID landlordID);

    List<RentalProperty> findByLandlordIDAndName(UUID landlordID, String name);

    // Find properties by rent less than a specified amount
    List<RentalProperty> findByRentLessThan(Double maxRent);

    // Find properties by name containing a specific string
    List<RentalProperty> findByNameContaining(String namePart);

    // Custom query to find properties by rent range
    @Query("SELECT rp FROM RentalProperty rp WHERE rp.rent BETWEEN :minRent AND :maxRent")
    List<RentalProperty> findByRentRange(@Param("minRent") Double minRent, @Param("maxRent") Double maxRent);

    // Custom query to find properties by name and sort by rent ascending
    @Query("SELECT rp FROM RentalProperty rp WHERE rp.name LIKE %:namePart% ORDER BY rp.rent ASC")
    List<RentalProperty> findByNameAndSortByRent(@Param("namePart") String namePart);

}
