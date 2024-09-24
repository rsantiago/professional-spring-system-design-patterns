package com.homeit.rental_proposal_service.events;

public record RentalProposalEvent(
        String proposalId,
        String roundId,
        String propertyId,
        String roundType) {
}