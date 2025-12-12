package com.tukaram.mv_backend.service.impl;

import com.tukaram.mv_backend.dto.BookingDto;
import com.tukaram.mv_backend.dto.BookingMapper;
import com.tukaram.mv_backend.dto.CreateBookingRequest;
import com.tukaram.mv_backend.exception.ResourceNotFoundException;
import com.tukaram.mv_backend.model.*;
import com.tukaram.mv_backend.model.enums.BookingStatus;
import com.tukaram.mv_backend.repository.*;
import com.tukaram.mv_backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookedSeatRepository bookedSeatRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public BookingDto createBooking(CreateBookingRequest req) {

        // validate show
        Show show = showRepository.findById(req.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + req.getShowId()));

        // validate seat ids exist and belong to same screen as show
        List<Long> seatIds = req.getSeatIds();
        List<Seat> seats = seatRepository.findByIdIn(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("One or more seatIds are invalid");
        }

        Long showScreenId = show.getScreen().getId();
        boolean anyNotInScreen = seats.stream().anyMatch(s -> !s.getScreen().getId().equals(showScreenId));
        if (anyNotInScreen) {
            throw new IllegalArgumentException("One or more seats do not belong to show's screen");
        }

        // check if any seat already booked for this show
        if (bookedSeatRepository.existsByShowIdAndSeatIdIn(show.getId(), seatIds)) {
            throw new IllegalStateException("One or more seats are already booked for this show");
        }

        // calculate amount: show.price * seat count
        BigDecimal total = show.getPrice().multiply(new BigDecimal(seatIds.size()));

        Booking booking = Booking.builder()
                .userId(req.getUserId())
                .show(show)
                .totalAmount(total)
                .status(BookingStatus.PENDING)
                .build();

        Booking savedBooking;
        try {
            savedBooking = bookingRepository.save(booking);

            // create booked seats (will fail if unique constraint violated by concurrent booking)
            List<BookedSeat> bookedSeats = seats.stream().map(s -> {
                return BookedSeat.builder()
                        .booking(savedBooking)
                        .show(show)
                        .seat(s)
                        .price(show.getPrice())
                        .build();
            }).collect(Collectors.toList());

            bookedSeatRepository.saveAll(bookedSeats);

            // mark booking confirmed (payment mock success)
            savedBooking.setStatus(BookingStatus.CONFIRMED);
            savedBooking.setBookedSeats(bookedSeats);
            bookingRepository.save(savedBooking);

        } catch (DataIntegrityViolationException dive) {
            // unique constraint violation or other DB integrity problem -> convert to meaningful message
            throw new IllegalStateException("Seat booking conflict detected. Some seats may have been booked concurrently. Try again.");
        }

        return BookingMapper.toDto(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBooking(Long id) {
        return bookingRepository.findById(id)
                .map(BookingMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (booking.getStatus() == BookingStatus.CANCELLED) return;

        // remove booked seats and set booking cancelled
        bookedSeatRepository.findByShowIdAndSeatIdIn(booking.getShow().getId(),
                        booking.getBookedSeats().stream().map(bs -> bs.getSeat().getId()).collect(Collectors.toList()))
                .forEach(bookedSeatRepository::delete);

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}
