package com.homeit.rental_proposal_service.dtos;

import com.homeit.rental_proposal_service.entities.Round;

import java.util.List;

public record RentalProposalDTO(String id, String tenantId, String landlordId,String propertyId,
                                List<Round> rounds, String status) {
}
