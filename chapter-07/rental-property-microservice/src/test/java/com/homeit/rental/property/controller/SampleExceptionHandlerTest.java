package com.homeit.rental.property.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeit.rental.property.dto.RentalPropertyDTO;
import com.homeit.rental.property.service.RentalPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class SampleExceptionHandlerTest {

    private final WebApplicationContext context;

    @MockBean
    @Qualifier("jpaRentalPropertyService")
    private RentalPropertyService rentalPropertyServiceMock;

    private final ObjectMapper objectMapper;

    @Autowired
    SampleExceptionHandlerTest(
            WebApplicationContext context,
            ObjectMapper objectMapper){
        this.context = context;
        this.objectMapper = objectMapper;
    }

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context).build();
    }

    @Test
    void testGetPropertyById() throws Exception {
        Mockito.when(rentalPropertyServiceMock.get(any()))
                .thenThrow(new RuntimeException("an unexpected 500 error"));
        mockMvc.perform(
                get("/api/v1/rental-properties/{id}",
                    UUID.randomUUID())
                .contentType("application/json"))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.title")
                .value("Customized Internal Server Error"));
        Mockito.verify(rentalPropertyServiceMock,
                times(1)).get(any());
    }

    @Test
    void testExceptionEndpoint() throws Exception {
        mockMvc.perform(
                get("/api/v1/rental-properties/error",
                    UUID.randomUUID())
                    .contentType("application/json"))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.title")
                .value("Customized Internal Server Error"));
    }

    @Test
    void testUpdateProperty() throws Exception {
        Mockito.when(rentalPropertyServiceMock.update(any(), any()))
            .thenThrow(
                new RuntimeException("an unexpected 500 error"));

        UUID createdUUID = UUID.randomUUID();
        RentalPropertyDTO updatedProperty = new RentalPropertyDTO(
                createdUUID, UUID.randomUUID(),
            "Updated Property","123 Updated St",
            "Updated City", "Updated Country",
            "54321", 1800.0);

        mockMvc.perform(
                put("/api/v1/rental-properties/{id}",
                    createdUUID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedProperty)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.title")
                    .value("Customized Internal Server Error"));
    }
}