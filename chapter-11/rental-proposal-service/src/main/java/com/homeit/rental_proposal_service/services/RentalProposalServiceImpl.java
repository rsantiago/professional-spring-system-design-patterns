package com.homeit.rental_proposal_service.services;
import com.homeit.rental_proposal_service.dtos.RentalProposalDTO;
import com.homeit.rental_proposal_service.entities.RentalProposal;
import com.homeit.rental_proposal_service.entities.RentalProposalStates;
import com.homeit.rental_proposal_service.entities.Round;
import com.homeit.rental_proposal_service.entities.RoundStates;
import com.homeit.rental_proposal_service.events.RentalProposalEvent;
import com.homeit.rental_proposal_service.repository.RentalProposalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RentalProposalServiceImpl implements RentalProposalService {

    private final RentalProposalRepository repository;
    private final KafkaTemplate<String, RentalProposalEvent> kafkaTemplate;
    @Value("${rental.proposal.topic.name:sample-name}")
    private String topicName;

    public RentalProposalServiceImpl(RentalProposalRepository repository, KafkaTemplate<String, RentalProposalEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<RentalProposalDTO> createProposal(RentalProposalDTO newRentalProposal) {
        Round firstRound = new Round(UUID.randomUUID().toString(),
            RoundStates.OPEN.toString(),
            newRentalProposal.tenantId(),
            null,
            null);
        List<Round> rounds = List.of(firstRound);

        return repository.save(new RentalProposal(UUID.randomUUID().toString(),
            newRentalProposal.tenantId(),
            newRentalProposal.landlordId(),
            newRentalProposal.propertyId(),
            rounds,
            RentalProposalStates.OPEN.toString()))
            .flatMap( proposal ->
                Mono.fromFuture(
                    kafkaTemplate.send(topicName,
                        new RentalProposalEvent(
                            proposal.id(),
                            firstRound.roundId(),
                            proposal.propertyId(),
                            firstRound.status()))
                    .thenApply(result -> proposal)))
            .map(RentalProposal::toDTO);
    }

    public Mono<RentalProposalDTO> addRound(String proposalId, Round round) {

        return repository.findById(proposalId)
            .flatMap(proposal -> {

                RentalProposal p = new RentalProposal(
                        proposal.id(),
                        proposal.tenantId(),
                        proposal.landlordId(),
                        proposal.propertyId(),
                        allRounds(round, proposal),
                        newProposalStatusFromRound(round)
                );

                return repository.save(p);
            })
            .flatMap(proposal ->
                Mono.fromFuture(kafkaTemplate.send("proposal-topic",
                    new RentalProposalEvent(
                        proposalId,
                        round.roundId(),
                        proposal.propertyId(),
                        round.status()))
                .thenApply(result -> proposal)))
            .map(RentalProposal::toDTO);
    }

    private static List<Round> allRounds(Round round, RentalProposal proposal) {
        List<Round> updatedRounds = new ArrayList<>(proposal.rounds());
        updatedRounds.add(new Round(
                UUID.randomUUID().toString(),
                round.status(),
                round.authorId(),
                round.value(),
                round.message()
        ));
        return updatedRounds;
    }

    private String newProposalStatusFromRound(Round round) {
        return switch (round.status()) {
            case "OFFER" -> "NEGOTIATING";
            case "COUNTER_OFFER" -> "NEGOTIATING";
            case "APPROVED" -> "APPROVED";
            case "REJECTED" -> "REJECTED";
            case "CANCELLED" -> "CANCELLED";
            default -> throw new IllegalArgumentException("Invalid round status: " + round.status());
        };
    }

    @Override
    public Flux<RentalProposalDTO> getProposals() {
        return repository.findAll().map(RentalProposal::toDTO);
    }

    @Override
    public Mono<RentalProposalDTO> deleteProposal(String proposalId) {
        return repository.findById(proposalId)
            .flatMap(proposal ->
                repository.deleteById(proposalId)
                .thenReturn(proposal.toDTO()));
    }

    @Override
    public Mono<RentalProposalDTO> getProposal(String proposalId) {
        return repository.findById(proposalId)
            .map(RentalProposal::toDTO);
    }
}
