package com.tukaram.mv_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukaram.mv_backend.model.Movie;
import com.tukaram.mv_backend.repository.MovieRepository;
import com.tukaram.mv_backend.test.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Example integration test that now relies on AbstractIntegrationTest.cleanupDatabase()
 * to clear DB before each test. Do NOT call deleteAll() here.
 */
@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupTestData() {
        // do NOT delete tables here — the base class already cleaned them
        // create any test data you need for each test
        movieRepository.save(Movie.builder()
                .title("Integration Movie")
                .durationMinutes(120)
                .language("English")
                .genre("Action")
                .build());
    }

    @Test
    void getAllMovies_shouldReturnAtLeastOne() throws Exception {
        mockMvc.perform(get("/api/movies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void postAndGetMovie_flow_shouldWork() throws Exception {
        Movie m = Movie.builder()
                .title("New Movie")
                .durationMinutes(90)
                .language("Hindi")
                .genre("Drama")
                .build();

        String payload = objectMapper.writeValueAsString(m);

        // create
        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("New Movie")));
    }
}
