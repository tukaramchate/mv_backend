package com.tukaram.mv_backend.dto;

import com.tukaram.mv_backend.model.BookedSeat;
import com.tukaram.mv_backend.model.Booking;

import java.util.stream.Collectors;

public class BookingMapper {

    public static BookedSeatDto toBookedSeatDto(BookedSeat b) {
        return BookedSeatDto.builder()
                .id(b.getId())
                .seatId(b.getSeat().getId())
                .showId(b.getShow().getId())
                .bookingId(b.getBooking().getId())
                .seatNumber(b.getSeat().getSeatNumber())
                .build();
    }

    public static BookingDto toDto(Booking b) {
        return BookingDto.builder()
                .id(b.getId())
                .userId(b.getUserId())
                .showId(b.getShow().getId())
                .totalAmount(b.getTotalAmount())
                .status(b.getStatus())
                .createdAt(b.getCreatedAt())
                .seats(b.getBookedSeats() == null ? null :
                        b.getBookedSeats().stream().map(BookingMapper::toBookedSeatDto).collect(Collectors.toList()))
                .build();
    }
}
