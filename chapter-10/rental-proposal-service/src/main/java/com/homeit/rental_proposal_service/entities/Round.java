package com.homeit.rental_proposal_service.entities;

public record Round (
    String roundId,
    String status, // OPEN, OFFER, COUNTER_OFFER, APPROVED, REJECTED, CANCELLED
    String authorId,
    Double value,
    String message) {

    // Getters and Setters
}