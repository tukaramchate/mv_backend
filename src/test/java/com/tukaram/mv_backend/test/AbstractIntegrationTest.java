package com.tukaram.mv_backend.test;

import com.tukaram.mv_backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Shared cleanup for integration tests.
 * Deletes children first, then parents to avoid foreign-key constraint errors.
 */
public abstract class AbstractIntegrationTest {

    @Autowired protected BookedSeatRepository bookedSeatRepository;
    @Autowired protected BookingRepository bookingRepository;
    @Autowired protected ShowRepository showRepository;
    @Autowired protected SeatRepository seatRepository;
    @Autowired protected ScreenRepository screenRepository;
    @Autowired protected TheatreRepository theatreRepository;
    @Autowired protected MovieRepository movieRepository;

    @BeforeEach
    public void cleanDatabase() {
        // delete in dependency order: children -> parents
        if (bookedSeatRepository != null) bookedSeatRepository.deleteAll();
        if (bookingRepository != null)     bookingRepository.deleteAll();
        if (showRepository != null)        showRepository.deleteAll();
        if (seatRepository != null)        seatRepository.deleteAll();
        if (screenRepository != null)      screenRepository.deleteAll();
        if (theatreRepository != null)     theatreRepository.deleteAll();
        if (movieRepository != null)       movieRepository.deleteAll();
    }
}
