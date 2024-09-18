package com.homeit.rental_proposal_service.services;

import com.homeit.rental_proposal_service.dtos.RentalProposalDTO;
import com.homeit.rental_proposal_service.entities.Round;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface RentalProposalService {
    Mono<RentalProposalDTO> createProposal(RentalProposalDTO newRentalProposal);
    Mono<RentalProposalDTO> addRound(String proposalId, Round round);

    Flux<RentalProposalDTO> getProposals();

    Mono<RentalProposalDTO> deleteProposal(String proposalId);

    Mono<RentalProposalDTO> getProposal(String proposalId);
}
