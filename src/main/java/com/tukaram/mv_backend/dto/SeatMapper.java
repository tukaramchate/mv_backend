package com.tukaram.mv_backend.dto;

import com.tukaram.mv_backend.model.Seat;

public class SeatMapper {

    public static SeatDto toDto(Seat s) {
        if (s == null) return null;
        return SeatDto.builder()
                .id(s.getId())
                .screenId(s.getScreen() != null ? s.getScreen().getId() : null)
                .seatNumber(s.getSeatNumber())
                .seatType(s.getSeatType())
                .rowNumber(s.getRowNumber())
                .colNumber(s.getColNumber())
                .build();
    }

    public static Seat fromCreateRequest(CreateSeatRequest req) {
        if (req == null) return null;
        return Seat.builder()
                .seatNumber(req.getSeatNumber())
                .seatType(req.getSeatType())
                .rowNumber(req.getRowNumber())
                .colNumber(req.getColNumber())
                .build();
    }

    public static void updateEntityFromRequest(Seat seat, CreateSeatRequest req) {
        if (req.getSeatNumber() != null) seat.setSeatNumber(req.getSeatNumber());
        if (req.getSeatType() != null) seat.setSeatType(req.getSeatType());
        if (req.getRowNumber() != null) seat.setRowNumber(req.getRowNumber());
        if (req.getColNumber() != null) seat.setColNumber(req.getColNumber());
    }
}
