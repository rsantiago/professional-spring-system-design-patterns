package com.ip.server.My.first.Springboot.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip.server.My.first.Springboot.server.dto.RentalPropertyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RentalPropertyControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context).build();
    }

    @Test
    void testGetAllProperties() throws Exception {
        mockMvc.perform(
            get("/api/v1/rental-properties")
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void testGetPropertyById() throws Exception {
        RentalPropertyDTO createdProperty = createProperty();

        mockMvc.perform(
                get("/api/v1/rental-properties/{id}",
                    createdProperty.id())
                .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name")
                .value("Test Property"));
    }

    private RentalPropertyDTO createProperty() throws Exception {
        RentalPropertyDTO property = new RentalPropertyDTO(
        null, UUID.randomUUID(),"Test Property",
        "123 Test St","Test City",
        "Test Country", "12345",1200.0);

        // Simulate creating a property
        String responseBody = mockMvc.perform(
                post("/api/v1/rental-properties")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(property)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        return objectMapper
            .readValue(responseBody, RentalPropertyDTO.class);
    }

    @Test
    void testCreateProperty() throws Exception {
        RentalPropertyDTO newProperty = new RentalPropertyDTO(
            UUID.randomUUID(), UUID.randomUUID(),
            "New Property", "456 New St",
            "New City", "New Country",
            "67890",1500.0);

        mockMvc.perform(
                post("/api/v1/rental-properties")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newProperty)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name")
                .value("New Property"));
    }

    @Test
    void testUpdateProperty() throws Exception {
        RentalPropertyDTO createdProperty = createProperty();

        RentalPropertyDTO updatedProperty = new RentalPropertyDTO(
            createdProperty.id(), UUID.randomUUID(),
            "Updated Property","123 Updated St",
            "Updated City", "Updated Country",
            "54321", 1800.0);

        mockMvc.perform(
                put("/api/v1/rental-properties/{id}",
                    createdProperty.id())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedProperty)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name")
                .value("Updated Property"));
    }

    @Test
    void testPatchProperty() throws Exception {
        RentalPropertyDTO createdProperty = createProperty();

        RentalPropertyDTO partialUpdate = new RentalPropertyDTO(
            createdProperty.id(), UUID.randomUUID(),
            "Partially Updated Property",
            null, null,
            null, null, null);

        mockMvc.perform(
                patch("/api/v1/rental-properties/{id}",
                    createdProperty.id())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(partialUpdate)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name")
                .value("Partially Updated Property"));
    }

    @Test
    void testDeleteProperty() throws Exception {
        RentalPropertyDTO property = createProperty();

        mockMvc.perform(
                delete("/api/v1/rental-properties/{id}",
                    property.id())
                .contentType("application/json"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testSearchProperties() throws Exception {
        String searchCity = "Test City";

        mockMvc.perform(
                get("/api/v1/rental-properties/search")
                .contentType("application/json")
                .param("city", searchCity))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].city").value("Test City"));
    }

    @Test
    void testGetHeaderInfo() throws Exception {
        String userAgent = "Test User-Agent";

        mockMvc.perform(
                get("/api/v1/rental-properties/headers")
                .header("User-Agent", userAgent)
                .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string("User-Agent: " + userAgent));
    }
}