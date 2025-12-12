package com.tukaram.mv_backend.dto;

import com.tukaram.mv_backend.model.Movie;

public class MovieMapper {

    public static MovieDto toDto(Movie m) {
        if (m == null) return null;
        return MovieDto.builder()
                .id(m.getId())
                .title(m.getTitle())
                .description(m.getDescription())
                .durationMinutes(m.getDurationMinutes())
                .language(m.getLanguage())
                .genre(m.getGenre())
                .posterUrl(m.getPosterUrl())
                .build();
    }

    public static Movie fromCreateRequest(CreateMovieRequest req) {
        if (req == null) return null;
        return Movie.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .durationMinutes(req.getDurationMinutes())
                .language(req.getLanguage())
                .genre(req.getGenre())
                .posterUrl(req.getPosterUrl())
                .build();
    }

    public static void updateEntityFromRequest(Movie movie, CreateMovieRequest req) {
        if (req.getTitle() != null) movie.setTitle(req.getTitle());
        if (req.getDescription() != null) movie.setDescription(req.getDescription());
        if (req.getDurationMinutes() != null) movie.setDurationMinutes(req.getDurationMinutes());
        if (req.getLanguage() != null) movie.setLanguage(req.getLanguage());
        if (req.getGenre() != null) movie.setGenre(req.getGenre());
        if (req.getPosterUrl() != null) movie.setPosterUrl(req.getPosterUrl());
    }
}
