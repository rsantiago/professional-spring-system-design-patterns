package com.homeit.rental_proposal_service.entities;

import com.homeit.rental_proposal_service.dtos.RentalProposalDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public record RentalProposal(
        @Id
        String id,
        String tenantId,
        String landlordId,
        String propertyId,
        List<Round> rounds,
        String status // OPEN, NEGOTIATING, ACCEPTED, REJECTED, CANCELLED
){
        public RentalProposalDTO toDTO() {
                return new RentalProposalDTO(this.id, this.tenantId, this.landlordId,this.propertyId, this.rounds(), this.status );
        }
}