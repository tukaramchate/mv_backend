package com.tukaram.mv_backend.service.impl;

import com.tukaram.mv_backend.dto.CreateMovieRequest;
import com.tukaram.mv_backend.dto.MovieDto;
import com.tukaram.mv_backend.dto.MovieMapper;
import com.tukaram.mv_backend.exception.ResourceNotFoundException;
import com.tukaram.mv_backend.model.Movie;
import com.tukaram.mv_backend.repository.MovieRepository;
import com.tukaram.mv_backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    @Transactional
    public MovieDto createMovie(CreateMovieRequest request) {
        Movie saved = movieRepository.save(MovieMapper.fromCreateRequest(request));
        return MovieMapper.toDto(saved);
    }

    @Override
    @Transactional
    public MovieDto updateMovie(Long id, CreateMovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        MovieMapper.updateEntityFromRequest(movie, request);
        movieRepository.save(movie);
        return MovieMapper.toDto(movie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDto getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(MovieMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(MovieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(MovieMapper::toDto)
                .collect(Collectors.toList());
    }
}
