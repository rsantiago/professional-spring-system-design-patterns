package com.homeit.rental.property.service;

import com.homeit.rental.property.dto.RentalPropertyDTO;
import com.homeit.rental.property.dto.utils.RentalPropertyConverter;
import com.homeit.rental.property.entities.RentalProperty;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.convert.DtoInstantiatingConverter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Qualifier("entityManagerRentalPropertyService")
public class RentalPropertyServiceEntityManagerImpl implements RentalPropertyService{

    private EntityManagerFactory entityManagerFactory;

    public RentalPropertyServiceEntityManagerImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<RentalPropertyDTO> getAllProperties() {
        throw new UnsupportedOperationException("This service only supports the DELETE operation.");
    }

    @Override
    public Page<RentalPropertyDTO> getPagedProperties(int page, int size) {
        throw new UnsupportedOperationException("This service only supports the DELETE operation.");
    }

    @Override
    public Optional<RentalPropertyDTO> get(UUID id) {
        throw new UnsupportedOperationException("This service only supports the DELETE operation.");
    }

    @Override
    public RentalPropertyDTO create(RentalPropertyDTO property) {
        throw new UnsupportedOperationException("This service only supports the DELETE operation.");
    }

    @Override
    public Optional<RentalPropertyDTO> update(UUID id, RentalPropertyDTO updatedProperty) {
        throw new UnsupportedOperationException("This service only supports the DELETE operation.");
    }

    @Override
    public Optional<RentalPropertyDTO> updateSomeFields(UUID id, RentalPropertyDTO partialUpdate) {
        throw new UnsupportedOperationException("This service only supports the DELETE operation.");
    }

    @Override
    public Optional<RentalPropertyDTO> delete(UUID id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        RentalPropertyDTO dto;
        try {
            transaction.begin();
            RentalProperty property = entityManager.find(RentalProperty.class, id);
            dto = RentalPropertyConverter.toDTO(property);
            entityManager.remove(property);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }

        return Optional.ofNullable(dto);
    }

    @Override
    public List<RentalPropertyDTO> search(String name, String address, String city, String country, String zipCode) {
        throw new UnsupportedOperationException("This service only supports the DELETE operation.");
    }
}
