package com.tukaram.mv_backend.service.impl;

import com.tukaram.mv_backend.dto.*;
import com.tukaram.mv_backend.dto.CreateMovieRequest;
import com.tukaram.mv_backend.dto.CreateScreenRequest;
import com.tukaram.mv_backend.dto.CreateShowRequest;
import com.tukaram.mv_backend.dto.CreateTheatreRequest;
import com.tukaram.mv_backend.model.*;
import com.tukaram.mv_backend.repository.*;
import com.tukaram.mv_backend.service.AdminService;
import com.tukaram.mv_backend.dto.MovieMapper;
import com.tukaram.mv_backend.dto.TheatreMapper;
import com.tukaram.mv_backend.dto.ScreenMapper;
import com.tukaram.mv_backend.dto.ShowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;

    private final MovieMapper movieMapper;
    private final TheatreMapper theatreMapper;
    private final ScreenMapper screenMapper;
    private final ShowMapper showMapper;

    // --- Movies ---
    @Override
    @Transactional
    public MovieDto createMovie(CreateMovieRequest req) {
        Movie movie = Movie.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .duration(req.getDuration())
                .language(req.getLanguage())
                .genre(req.getGenre())
                .posterUrl(req.getPosterUrl())
                .build();

        Movie saved = movieRepository.save(movie);
        return movieMapper.toDto(saved);
    }

    @Override
    @Transactional
    public MovieDto updateMovie(Long movieId, CreateMovieRequest req) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + movieId));

        if (req.getTitle() != null) movie.setTitle(req.getTitle());
        if (req.getDescription() != null) movie.setDescription(req.getDescription());
        if (req.getDuration() != null) movie.setDuration(req.getDuration());
        if (req.getLanguage() != null) movie.setLanguage(req.getLanguage());
        if (req.getGenre() != null) movie.setGenre(req.getGenre());
        if (req.getPosterUrl() != null) movie.setPosterUrl(req.getPosterUrl());

        Movie saved = movieRepository.save(movie);
        return movieMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteMovie(Long movieId) {
        // protect referential integrity: don't delete if shows exist
        long showCount = showRepository.countByMovieId(movieId);
        if (showCount > 0) {
            throw new IllegalStateException("Cannot delete movie: there are " + showCount + " shows referencing it.");
        }
        movieRepository.deleteById(movieId);
    }

    // --- Theatres & Screens ---
    @Override
    @Transactional
    public TheatreDto createTheatre(CreateTheatreRequest req) {
        Theatre t = Theatre.builder()
                .name(req.getName())
                .location(req.getLocation())
                .build();
        Theatre saved = theatreRepository.save(t);
        return theatreMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ScreenDto createScreen(CreateScreenRequest req) {
        Theatre theatre = theatreRepository.findById(req.getTheatreId())
                .orElseThrow(() -> new IllegalArgumentException("Theatre not found: " + req.getTheatreId()));

        Screen s = Screen.builder()
                .theatre(theatre)
                .screenName(req.getScreenName())
                .totalSeats(req.getTotalSeats())
                .build();

        Screen saved = screenRepository.save(s);
        return screenMapper.toDto(saved);
    }

    // --- Shows ---
    @Override
    @Transactional
    public ShowDto createShow(CreateShowRequest req) {
        Movie movie = movieRepository.findById(req.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + req.getMovieId()));
        Theatre theatre = theatreRepository.findById(req.getTheatreId())
                .orElseThrow(() -> new IllegalArgumentException("Theatre not found: " + req.getTheatreId()));
        Screen screen = screenRepository.findById(req.getScreenId())
                .orElseThrow(() -> new IllegalArgumentException("Screen not found: " + req.getScreenId()));

        Show show = Show.builder()
                .movie(movie)
                .theatre(theatre)
                .screen(screen)
                .showTime(req.getShowTime())
                .price(req.getPrice())
                .build();

        Show saved = showRepository.save(show);
        return showMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteShow(Long showId) {
        long bookingCount = bookingRepository.countByShowId(showId);
        if (bookingCount > 0) {
            throw new IllegalStateException("Cannot delete show: there are " + bookingCount + " bookings.");
        }
        showRepository.deleteById(showId);
    }
}
