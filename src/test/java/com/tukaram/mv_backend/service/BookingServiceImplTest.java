package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.BookingDto;
import com.tukaram.mv_backend.dto.CreateBookingRequest;
import com.tukaram.mv_backend.model.*;
import com.tukaram.mv_backend.model.enums.BookingStatus;
import com.tukaram.mv_backend.model.enums.SeatType;
import com.tukaram.mv_backend.repository.BookedSeatRepository;
import com.tukaram.mv_backend.repository.BookingRepository;
import com.tukaram.mv_backend.repository.SeatRepository;
import com.tukaram.mv_backend.repository.ShowRepository;
import com.tukaram.mv_backend.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookedSeatRepository bookedSeatRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Show createShow(long id, Screen screen, BigDecimal price) {
        Movie movie = Movie.builder().id(10L).title("M").build();
        Theatre theatre = Theatre.builder().id(20L).name("T").build();
        return Show.builder()
                .id(id)
                .movie(movie)
                .theatre(theatre)
                .screen(screen)
                .showTime(LocalDateTime.now().plusDays(1))
                .price(price)
                .available(true)
                .build();
    }

    private Seat seat(long id, Screen screen, String seatNumber) {
        return Seat.builder()
                .id(id)
                .screen(screen)
                .seatNumber(seatNumber)
                .seatType(SeatType.REGULAR)
                .build();
    }

    @Test
    void createBooking_success() {
        Screen screen = Screen.builder().id(100L).screenName("S1").build();
        Show show = createShow(1L, screen, new BigDecimal("200.00"));

        // seat list
        Seat s1 = seat(1001L, screen, "A1");
        Seat s2 = seat(1002L, screen, "A2");
        List<Long> seatIds = List.of(s1.getId(), s2.getId());

        // mocks
        when(showRepository.findById(1L)).thenReturn(java.util.Optional.of(show));
        when(seatRepository.findByIdIn(seatIds)).thenReturn(List.of(s1, s2));
        when(bookedSeatRepository.existsByShowIdAndSeatIdIn(show.getId(), seatIds)).thenReturn(false);

        // bookingRepository.save should return the same booking but with id assigned
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            if (b.getId() == null) b.setId(555L);
            return b;
        });

        // bookedSeatRepository.saveAll should return seats with generated ids (simulate DB)
        when(bookedSeatRepository.saveAll(anyList())).thenAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            List<BookedSeat> list = (List<BookedSeat>) invocation.getArgument(0);
            long idx = 1;
            for (BookedSeat bs : list) {
                if (bs.getId() == null) bs.setId(100L + idx++);
            }
            return list;
        });

        CreateBookingRequest req = CreateBookingRequest.builder()
                .showId(show.getId())
                .userId(42L)
                .seatIds(seatIds)
                .paymentInfo("mock")
                .build();

        BookingDto dto = bookingService.createBooking(req);

        assertNotNull(dto);
        assertEquals(555L, dto.getId());
        assertEquals(BookingStatus.CONFIRMED, dto.getStatus());
        assertNotNull(dto.getSeats());
        assertEquals(2, dto.getSeats().size());

        verify(showRepository, times(1)).findById(1L);
        verify(seatRepository, times(1)).findByIdIn(seatIds);
        verify(bookedSeatRepository, times(1)).existsByShowIdAndSeatIdIn(show.getId(), seatIds);
        verify(bookingRepository, atLeastOnce()).save(any(Booking.class));
        verify(bookedSeatRepository, times(1)).saveAll(anyList());
    }

    @Test
    void createBooking_seatMissing_shouldThrow() {
        Screen screen = Screen.builder().id(100L).screenName("S1").build();
        Show show = createShow(1L, screen, new BigDecimal("150.00"));

        List<Long> seatIds = List.of(999L);

        when(showRepository.findById(1L)).thenReturn(java.util.Optional.of(show));
        when(seatRepository.findByIdIn(seatIds)).thenReturn(List.of()); // seat missing

        CreateBookingRequest req = CreateBookingRequest.builder()
                .showId(show.getId())
                .userId(1L)
                .seatIds(seatIds)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(req));
        assertTrue(ex.getMessage().toLowerCase().contains("invalid"));

        verify(seatRepository, times(1)).findByIdIn(seatIds);
        verify(bookedSeatRepository, never()).saveAll(anyList());
    }

    @Test
    void createBooking_alreadyBooked_shouldThrow() {
        Screen screen = Screen.builder().id(100L).screenName("S1").build();
        Show show = createShow(1L, screen, new BigDecimal("150.00"));

        Seat s1 = seat(2001L, screen, "B1");
        List<Long> seatIds = List.of(s1.getId());

        when(showRepository.findById(1L)).thenReturn(java.util.Optional.of(show));
        when(seatRepository.findByIdIn(seatIds)).thenReturn(List.of(s1));
        when(bookedSeatRepository.existsByShowIdAndSeatIdIn(show.getId(), seatIds)).thenReturn(true);

        CreateBookingRequest req = CreateBookingRequest.builder()
                .showId(show.getId())
                .userId(2L)
                .seatIds(seatIds)
                .build();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> bookingService.createBooking(req));
        assertTrue(ex.getMessage().toLowerCase().contains("already booked"));

        verify(bookedSeatRepository, times(1)).existsByShowIdAndSeatIdIn(show.getId(), seatIds);
        verify(bookedSeatRepository, never()).saveAll(anyList());
    }

    @Test
    void createBooking_dataIntegrityViolation_shouldTranslateToIllegalState() {
        Screen screen = Screen.builder().id(100L).screenName("S1").build();
        Show show = createShow(1L, screen, new BigDecimal("150.00"));

        Seat s1 = seat(3001L, screen, "C1");
        List<Long> seatIds = List.of(s1.getId());

        when(showRepository.findById(1L)).thenReturn(java.util.Optional.of(show));
        when(seatRepository.findByIdIn(seatIds)).thenReturn(List.of(s1));
        when(bookedSeatRepository.existsByShowIdAndSeatIdIn(show.getId(), seatIds)).thenReturn(false);

        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(777L);
            return b;
        });

        // Simulate DB unique constraint violation on saveAll
        when(bookedSeatRepository.saveAll(anyList())).thenThrow(new DataIntegrityViolationException("Unique constraint"));

        CreateBookingRequest req = CreateBookingRequest.builder()
                .showId(show.getId())
                .userId(3L)
                .seatIds(seatIds)
                .build();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> bookingService.createBooking(req));
        assertTrue(ex.getMessage().toLowerCase().contains("conflict"));

        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(bookedSeatRepository, times(1)).saveAll(anyList());
    }
}
