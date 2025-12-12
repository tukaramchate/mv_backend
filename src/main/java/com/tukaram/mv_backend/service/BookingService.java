package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.BookingDto;
import com.tukaram.mv_backend.dto.CreateBookingRequest;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(CreateBookingRequest request);

    BookingDto getBooking(Long id);

    List<BookingDto> getBookingsByUser(Long userId);

    void cancelBooking(Long id);
}
