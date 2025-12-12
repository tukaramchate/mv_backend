package com.tukaram.mv_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukaram.mv_backend.dto.CreateBookingRequest;
import com.tukaram.mv_backend.model.*;
import com.tukaram.mv_backend.model.enums.SeatType;
import com.tukaram.mv_backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Show show;
    private List<Seat> seats;

    @BeforeEach
    void setup() {
        bookingRepository.deleteAll();
        showRepository.deleteAll();
        seatRepository.deleteAll();
        screenRepository.deleteAll();
        theatreRepository.deleteAll();
        movieRepository.deleteAll();

        Movie movie = movieRepository.save(Movie.builder()
                .title("Integration Movie")
                .durationMinutes(100)
                .build());

        Theatre theatre = theatreRepository.save(Theatre.builder()
                .name("T-Int")
                .location("L")
                .build());

        Screen screen = screenRepository.save(Screen.builder()
                .theatre(theatre)
                .screenName("S-Int")
                .totalSeats(10)
                .build());

        seats = List.of(
                Seat.builder().screen(screen).seatNumber("A1").seatType(SeatType.REGULAR).build(),
                Seat.builder().screen(screen).seatNumber("A2").seatType(SeatType.REGULAR).build()
        );
        seats = seatRepository.saveAll(seats);

        show = showRepository.save(Show.builder()
                .movie(movie)
                .theatre(theatre)
                .screen(screen)
                .showTime(LocalDateTime.now().plusDays(1))
                .price(new BigDecimal("120.00"))
                .available(true)
                .build());
    }

    @Test
    void createBooking_endToEnd_shouldReturnConfirmedBooking() throws Exception {
        CreateBookingRequest req = CreateBookingRequest.builder()
                .showId(show.getId())
                .userId(123L)
                .seatIds(seats.stream().map(Seat::getId).collect(Collectors.toList()))
                .paymentInfo("mock")
                .build();

        // POST booking
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.status", is("CONFIRMED")))
                .andExpect(jsonPath("$.seats", hasSize(seats.size())));

        // GET bookings by user
        mockMvc.perform(get("/api/bookings/user/{userId}", 123L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void createBooking_doubleBook_shouldReturnConflict() throws Exception {
        // first booking
        CreateBookingRequest req1 = CreateBookingRequest.builder()
                .showId(show.getId())
                .userId(200L)
                .seatIds(List.of(seats.get(0).getId()))
                .paymentInfo("mock")
                .build();

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("CONFIRMED")));

        // second booking same seat - should fail (CONFLICT)
        CreateBookingRequest req2 = CreateBookingRequest.builder()
                .showId(show.getId())
                .userId(201L)
                .seatIds(List.of(seats.get(0).getId()))
                .paymentInfo("mock")
                .build();

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isConflict());
    }
}
