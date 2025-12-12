package com.tukaram.mv_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukaram.mv_backend.dto.CreateMovieRequest;
import com.tukaram.mv_backend.model.Movie;
import com.tukaram.mv_backend.repository.MovieRepository;
import org.junit.jupiter.api.*;
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
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        movieRepository.deleteAll();
    }

    @Test
    void postAndGetMovie_flow_shouldWork() throws Exception {
        CreateMovieRequest req = CreateMovieRequest.builder()
                .title("Integration Movie")
                .description("Integration desc")
                .durationMinutes(95)
                .language("English")
                .genre("Drama")
                .posterUrl("http://poster")
                .build();

        // Create movie (POST)
        String postBody = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/movies/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Integration Movie")));

        // Ensure repository has one movie
        Movie saved = movieRepository.findAll().get(0);

        // GET the created movie
        mockMvc.perform(get("/api/movies/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Integration Movie")));
    }

    @Test
    void getAllMovies_shouldReturnEmptyListInitially() throws Exception {
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void searchByTitle_shouldReturnMatches() throws Exception {
        Movie m1 = Movie.builder().title("Avengers").durationMinutes(120).build();
        Movie m2 = Movie.builder().title("Average Joe").durationMinutes(90).build();
        movieRepository.save(m1);
        movieRepository.save(m2);

        mockMvc.perform(get("/api/movies").param("q", "Aveng"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Avengers")));
    }
}
