package com.tukaram.mv_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukaram.mv_backend.dto.CreateSeatRequest;
import com.tukaram.mv_backend.model.Screen;
import com.tukaram.mv_backend.model.Theatre;
import com.tukaram.mv_backend.model.enums.SeatType;
import com.tukaram.mv_backend.repository.ScreenRepository;
import com.tukaram.mv_backend.repository.SeatRepository;
import com.tukaram.mv_backend.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SeatControllerIntegrationTest {

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
    void setup() {
        seatRepository.deleteAll();
        screenRepository.deleteAll();
        theatreRepository.deleteAll();

        Theatre t = theatreRepository.save(Theatre.builder().name("T1").location("L").build());
        screen = screenRepository.save(Screen.builder().theatre(t).screenName("S1").totalSeats(10).build());
    }

    @Test
    void createAndListSeats_shouldWork() throws Exception {
        CreateSeatRequest req = CreateSeatRequest.builder()
                .screenId(screen.getId())
                .seatNumber("A1")
                .seatType(SeatType.REGULAR)
                .rowNumber(1)
                .colNumber(1)
                .build();

        mockMvc.perform(post("/api/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatNumber", is("A1")))
                .andExpect(jsonPath("$.screenId", is(screen.getId().intValue())));

        mockMvc.perform(get("/api/seats").param("screenId", screen.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
