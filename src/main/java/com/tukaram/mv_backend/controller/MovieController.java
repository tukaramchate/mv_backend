package com.tukaram.mv_backend.controller;

import com.tukaram.mv_backend.dto.CreateMovieRequest;
import com.tukaram.mv_backend.dto.MovieDto;
import com.tukaram.mv_backend.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAll(@RequestParam(value = "q", required = false) String q) {
        List<MovieDto> result =
                (q == null || q.isBlank())
                        ? movieService.getAllMovies()
                        : movieService.searchByTitle(q);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    public ResponseEntity<MovieDto> create(@Valid @RequestBody CreateMovieRequest request) {
        MovieDto created = movieService.createMovie(request);
        return ResponseEntity.created(URI.create("/api/movies/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateMovieRequest request) {
        return ResponseEntity.ok(movieService.updateMovie(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
