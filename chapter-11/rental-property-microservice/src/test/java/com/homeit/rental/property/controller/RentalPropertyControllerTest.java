package com.homeit.rental.property.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeit.rental.property.dto.RentalPropertyDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RentalPropertyControllerTest {

    @MockBean
    private JwtDecoder jwtDecoder;

    private final WebApplicationContext context;
    private final ObjectMapper objectMapper;

    @BeforeEach
    public void securitySetup() {
        // Mock the SecurityContext and Authentication
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("SCOPE_rental_properties:write"));
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "testUser";
            }
        };

        // Set the mocked authentication in the SecurityContext
//        when(securityContext.getAuthentication()).thenReturn(authentication);
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    @Autowired
    RentalPropertyControllerTest(
            WebApplicationContext context,
            ObjectMapper objectMapper){
        this.context = context;
        this.objectMapper = objectMapper;
    }

    private RentalPropertyDTO createdProperty;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context).build();
        createdProperty = createProperty();
    }

    @Test
    void testGetAllProperties() throws Exception {
        mockMvc.perform(
            get("/api/v1/rental-properties?page=1&size=2")
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageable.pageNumber").value(1))
            .andExpect(jsonPath("$.content[*].name").exists())
            .andExpect(jsonPath("$.pageable.pageSize").value(2))
        ;
    }

    @Test
    void testGetPropertyById() throws Exception {
        mockMvc.perform(
                get("/api/v1/rental-properties/{id}",
                    createdProperty.id())
                .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name")
                .value("Test Property Chapter 7"));
    }

    private RentalPropertyDTO createProperty() throws Exception {
        RentalPropertyDTO property = new RentalPropertyDTO(
        null, UUID.randomUUID(),"Test Property Chapter 7",
        "123 Test St","Test City Chapter 7",
        "Test Country", "12345", 1,1200.0);

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
            "67890",1, 1500.0);

        mockMvc.perform(
                post("/api/v1/rental-properties")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newProperty)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name")
                .value("New Property"));
    }

    @Test
    void testCreatePropertyErrorNullAddress() throws Exception {
        RentalPropertyDTO newProperty = new RentalPropertyDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                "New Property", null,
                "New City", "New Country",
                "67890",1, 1500.0);

        mockMvc.perform(
                post("/api/v1/rental-properties")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(newProperty)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProperty() throws Exception {
        RentalPropertyDTO updatedProperty = new RentalPropertyDTO(
            createdProperty.id(), UUID.randomUUID(),
            "Updated Property","123 Updated St",
            "Updated City", "Updated Country",
            "54321", 1, 1800.0);

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
        RentalPropertyDTO partialUpdate = new RentalPropertyDTO(
            createdProperty.id(), UUID.randomUUID(),
            "Partially Updated Property",
            null, null,
            null, null, 1, null);

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
        RentalPropertyDTO propertyToDelete = createProperty();

        mockMvc.perform(
                delete("/api/v1/rental-properties/{id}",
                    propertyToDelete.id())
                .contentType("application/json"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testSearchProperties() throws Exception {
        String searchCity = "ity";

        mockMvc.perform(
                get("/api/v1/rental-properties/search")
                .contentType("application/json")
                .param("city", searchCity))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].city").value("Test City Chapter 7"));
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