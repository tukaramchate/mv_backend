package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.CreateSeatRequest;
import com.tukaram.mv_backend.dto.SeatDto;

import java.util.List;

public interface SeatService {
    SeatDto createSeat(CreateSeatRequest request);
    List<SeatDto> createSeatsBulk(List<CreateSeatRequest> requests);
    SeatDto updateSeat(Long id, CreateSeatRequest request);
    void deleteSeat(Long id);
    SeatDto getById(Long id);
    List<SeatDto> getByScreenId(Long screenId);
}
