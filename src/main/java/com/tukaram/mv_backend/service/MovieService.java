package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.CreateMovieRequest;
import com.tukaram.mv_backend.dto.MovieDto;

import java.util.List;

public interface MovieService {
    MovieDto createMovie(CreateMovieRequest request);
    MovieDto updateMovie(Long id, CreateMovieRequest request);
    void deleteMovie(Long id);
    MovieDto getMovieById(Long id);
    List<MovieDto> getAllMovies();
    List<MovieDto> searchByTitle(String title);
}
