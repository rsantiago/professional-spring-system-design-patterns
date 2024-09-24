package com.homeit.rental.property.events;

public record RentalProposalEvent(
        String proposalId,
        String roundId,
        String propertyId,
        String roundType) {
}