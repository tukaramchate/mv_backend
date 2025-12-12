package com.tukaram.mv_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukaram.mv_backend.model.Screen;
import com.tukaram.mv_backend.model.Theatre;
import com.tukaram.mv_backend.repository.ScreenRepository;
import com.tukaram.mv_backend.repository.SeatRepository;
import com.tukaram.mv_backend.repository.TheatreRepository;
import com.tukaram.mv_backend.test.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Seat endpoints. Relies on AbstractIntegrationTest for cleanup.
 * Uses raw JSON payload for bulk seat creation to avoid constructor mismatch.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SeatControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Screen screen;

    @BeforeEach
    void setupTestData() {
        // AbstractIntegrationTest.cleanDatabase() already ran before this method.
        Theatre theatre = theatreRepository.save(Theatre.builder()
                .name("Seat-Test-Theatre")
                .location("Test-Loc")
                .build());

        screen = screenRepository.save(Screen.builder()
                .theatre(theatre)
                .screenName("Screen-1")
                .totalSeats(10)
                .build());
    }

    @Test
    void createAndListSeats_shouldWork() throws Exception {
        // Build raw JSON array payload so we don't depend on test DTO constructors.
        String payload = "[" +
                "{\"screenId\":" + screen.getId() + ",\"seatNumber\":\"A1\",\"seatType\":\"REGULAR\"}," +
                "{\"screenId\":" + screen.getId() + ",\"seatNumber\":\"A2\",\"seatType\":\"REGULAR\"}" +
                "]";

        mockMvc.perform(post("/api/seats/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(2)));

        // verify via GET by screen
        mockMvc.perform(get("/api/seats/screen/{screenId}", screen.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", notNullValue()));
    }
}
