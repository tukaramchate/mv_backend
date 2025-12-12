package com.tukaram.mv_backend.controller;

import com.tukaram.mv_backend.dto.CreateTheatreRequest;
import com.tukaram.mv_backend.dto.TheatreDto;
import com.tukaram.mv_backend.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;

    @GetMapping
    public ResponseEntity<List<TheatreDto>> getAll() {
        return ResponseEntity.ok(theatreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(theatreService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TheatreDto> create(@Valid @RequestBody CreateTheatreRequest request) {
        TheatreDto created = theatreService.createTheatre(request);
        return ResponseEntity.created(URI.create("/api/theatres/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheatreDto> update(@PathVariable Long id, @Valid @RequestBody CreateTheatreRequest request) {
        return ResponseEntity.ok(theatreService.updateTheatre(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return ResponseEntity.noContent().build();
    }
}
