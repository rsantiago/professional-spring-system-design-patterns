package com.homeit.rental_proposal_service;

import com.homeit.rental_proposal_service.controllers.RentalProposalController;
import com.homeit.rental_proposal_service.services.RentalProposalServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@ContextConfiguration(classes = {RentalProposalServiceImpl.class, RentalProposalController.class
        //,EmbeddedMongoConfig.class
})
public class RentalProposalControllerTest {

//    @Autowired
//    private WebTestClient webTestClient;

    @Test
    public void testAddRound() {
//        Round round = new Round();
//        round.setType("OFFER");
//        round.setAuthor("tenant1");
//        round.setValue(1200.00);
//        round.setMessage("First offer");
//
//        webTestClient.post()
//                .uri("/proposals/{proposalId}/rounds", "some-proposal-id")
//                .bodyValue(round)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.rounds[0].type").isEqualTo("OFFER");
    }
}