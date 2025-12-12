package com.tukaram.mv_backend.dto;

import com.tukaram.mv_backend.model.Show;

public class ShowMapper {

    public static ShowDto toDto(Show s) {
        return ShowDto.builder()
                .id(s.getId())
                .movieId(s.getMovie().getId())
                .theatreId(s.getTheatre().getId())
                .screenId(s.getScreen().getId())
                .showTime(s.getShowTime())
                .price(s.getPrice())
                .language(s.getLanguage())
                .available(s.isAvailable())
                .build();
    }
}
