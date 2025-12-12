package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.*;
import com.tukaram.mv_backend.dto.*; // keep if you have DTOs in root dto package
import com.tukaram.mv_backend.dto.CreateMovieRequest;
import com.tukaram.mv_backend.dto.CreateScreenRequest;
import com.tukaram.mv_backend.dto.CreateShowRequest;
import com.tukaram.mv_backend.dto.CreateTheatreRequest;

public interface AdminService {

    // Movies
    MovieDto createMovie(CreateMovieRequest req);
    MovieDto updateMovie(Long movieId, CreateMovieRequest req);
    void deleteMovie(Long movieId);

    // Theatres / screens
    TheatreDto createTheatre(CreateTheatreRequest req);
    ScreenDto createScreen(CreateScreenRequest req);

    // Shows
    ShowDto createShow(CreateShowRequest req);
    void deleteShow(Long showId);
}
