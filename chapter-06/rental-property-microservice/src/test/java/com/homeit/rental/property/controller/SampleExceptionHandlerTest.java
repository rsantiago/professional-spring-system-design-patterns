package com.homeit.rental.property.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeit.rental.property.dto.RentalPropertyDTO;
import com.homeit.rental.property.service.RentalPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @Test
//    void testPatchProperty() throws Exception {
//        RentalPropertyDTO partialUpdate = new RentalPropertyDTO(
//            createdProperty.id(), UUID.randomUUID(),
//            "Partially Updated Property",
//            null, null,
//            null, null, null);
//
//        mockMvc.perform(
//                patch("/api/v1/rental-properties/{id}",
//                    createdProperty.id())
//                .contentType("application/json")
//                .content(objectMapper.writeValueAsString(partialUpdate)))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.name")
//                .value("Partially Updated Property"));
//    }
//
//    @Test
//    void testDeleteProperty() throws Exception {
//        RentalPropertyDTO propertyToDelete = createProperty();
//
//        mockMvc.perform(
//                delete("/api/v1/rental-properties/{id}",
//                    propertyToDelete.id())
//                .contentType("application/json"))
//            .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testSearchProperties() throws Exception {
//        String searchCity = "Test City";
//
//        mockMvc.perform(
//                get("/api/v1/rental-properties/search")
//                .contentType("application/json")
//                .param("city", searchCity))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$[0].city").value("Test City"));
//    }
//
//    @Test
//    void testGetHeaderInfo() throws Exception {
//        String userAgent = "Test User-Agent";
//
//        mockMvc.perform(
//                get("/api/v1/rental-properties/headers")
//                .header("User-Agent", userAgent)
//                .contentType("application/json"))
//            .andExpect(status().isOk())
//            .andExpect(content().string("User-Agent: " + userAgent));
//    }
}