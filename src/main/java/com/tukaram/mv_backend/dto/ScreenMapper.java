package com.tukaram.mv_backend.dto;

import com.tukaram.mv_backend.model.Screen;

public class ScreenMapper {

    public static ScreenDto toDto(Screen s) {
        if (s == null) return null;
        return ScreenDto.builder()
                .id(s.getId())
                .theatreId(s.getTheatre() != null ? s.getTheatre().getId() : null)
                .screenName(s.getScreenName())
                .totalSeats(s.getTotalSeats())
                .seatMap(s.getSeatMap())
                .build();
    }

    public static Screen fromCreateRequest(CreateScreenRequest req) {
        if (req == null) return null;
        return Screen.builder()
                .screenName(req.getScreenName())
                .totalSeats(req.getTotalSeats())
                .seatMap(req.getSeatMap())
                .build();
    }

    public static void updateEntityFromRequest(Screen screen, CreateScreenRequest req) {
        if (req.getScreenName() != null) screen.setScreenName(req.getScreenName());
        if (req.getTotalSeats() != null) screen.setTotalSeats(req.getTotalSeats());
        if (req.getSeatMap() != null) screen.setSeatMap(req.getSeatMap());
    }
}
