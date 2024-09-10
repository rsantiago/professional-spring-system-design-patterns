package com.homeit.rental_proposal_service.repository;

import com.homeit.rental_proposal_service.entities.RentalProposal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RentalProposalRepository extends ReactiveMongoRepository<RentalProposal, String> {
    Mono<RentalProposal> findByTenantIdAndLandlordIdAndPropertyId(String tenantId, String landlordId, String propertyId);
}