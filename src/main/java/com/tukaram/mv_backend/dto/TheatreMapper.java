package com.tukaram.mv_backend.dto;

import com.tukaram.mv_backend.model.Theatre;

public class TheatreMapper {

    public static TheatreDto toDto(Theatre t) {
        if (t == null) return null;
        return TheatreDto.builder()
                .id(t.getId())
                .name(t.getName())
                .location(t.getLocation())
                .build();
    }

    public static Theatre fromCreateRequest(CreateTheatreRequest req) {
        if (req == null) return null;
        return Theatre.builder()
                .name(req.getName())
                .location(req.getLocation())
                .build();
    }

    public static void updateEntityFromRequest(Theatre theatre, CreateTheatreRequest req) {
        if (req.getName() != null) theatre.setName(req.getName());
        if (req.getLocation() != null) theatre.setLocation(req.getLocation());
    }
}
