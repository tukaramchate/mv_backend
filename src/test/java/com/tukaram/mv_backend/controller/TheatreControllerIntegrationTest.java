package com.tukaram.mv_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukaram.mv_backend.model.Theatre;
import com.tukaram.mv_backend.repository.TheatreRepository;
import com.tukaram.mv_backend.test.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Theatre endpoints. Relies on AbstractIntegrationTest for cleanup.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TheatreControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupTestData() {
        // cleanup already executed by base class
        theatreRepository.save(Theatre.builder()
                .name("Theatre-Int-1")
                .location("Loc-1")
                .build());
    }

    @Test
    void createAndGetTheatre_shouldWork() throws Exception {
        Theatre t = Theatre.builder().name("New T").location("L2").build();
        String payload = objectMapper.writeValueAsString(t);

        mockMvc.perform(post("/api/theatres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("New T")));

        mockMvc.perform(get("/api/theatres")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }
}
