package com.tukaram.mv_backend.controller;

import com.tukaram.mv_backend.dto.CreateShowRequest;
import com.tukaram.mv_backend.dto.ShowDto;
import com.tukaram.mv_backend.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @PostMapping
    public ResponseEntity<ShowDto> create(@Valid @RequestBody CreateShowRequest req) {
        ShowDto created = showService.createShow(req);
        return ResponseEntity.created(URI.create("/api/shows/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ShowDto>> getAll() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowDto>> getByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showService.getShowsByMovie(movieId));
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<ShowDto>> getByTheatre(@PathVariable Long theatreId) {
        return ResponseEntity.ok(showService.getShowsByTheatre(theatreId));
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<ShowDto>> getByScreen(@PathVariable Long screenId) {
        return ResponseEntity.ok(showService.getShowsByScreen(screenId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowDto> update(@PathVariable Long id, @Valid @RequestBody CreateShowRequest req) {
        return ResponseEntity.ok(showService.updateShow(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }
}
