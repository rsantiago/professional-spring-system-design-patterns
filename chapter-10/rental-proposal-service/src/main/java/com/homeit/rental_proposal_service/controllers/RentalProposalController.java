package com.homeit.rental_proposal_service.controllers;

import com.homeit.rental_proposal_service.dtos.RentalProposalDTO;
import com.homeit.rental_proposal_service.entities.Round;
import com.homeit.rental_proposal_service.services.RentalProposalServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/proposals")
public class RentalProposalController {

    private final RentalProposalServiceImpl service;

    public RentalProposalController(RentalProposalServiceImpl service) {
        this.service = service;
    }

    @PostMapping()
    public Mono<RentalProposalDTO> createProposal(@RequestBody RentalProposalDTO rentalProposalDTO) {
        return service.createProposal(rentalProposalDTO);
    }

    @PostMapping("/{proposalId}/rounds")
    public Mono<ResponseEntity<RentalProposalDTO>> addRound(@PathVariable String proposalId, @RequestBody Round round) {
        return service.addRound(proposalId, round)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<RentalProposalDTO>>> getProposals() {
        return Mono.just(ResponseEntity.ok(service.getProposals()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/{proposalId}")
    public Mono<ResponseEntity<RentalProposalDTO>> getProposals(@PathVariable String proposalId) {
        return service.getProposal(proposalId)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{proposalId}")
    public Mono<ResponseEntity<RentalProposalDTO>> deleteProposal(@PathVariable String proposalId) {
        return service.deleteProposal(proposalId)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}