package com.tukaram.mv_backend.controller;

import com.tukaram.mv_backend.dto.*;
import com.tukaram.mv_backend.dto.CreateMovieRequest;
import com.tukaram.mv_backend.dto.CreateScreenRequest;
import com.tukaram.mv_backend.dto.CreateShowRequest;
import com.tukaram.mv_backend.dto.CreateTheatreRequest;
import com.tukaram.mv_backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Movies
    @PostMapping("/movies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDto> createMovie(@RequestBody CreateMovieRequest req) {
        MovieDto dto = adminService.createMovie(req);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/movies/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable("id") Long id, @RequestBody CreateMovieRequest req) {
        MovieDto dto = adminService.updateMovie(id, req);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/movies/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable("id") Long id) {
        adminService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // Theatre / screen
    @PostMapping("/theatres")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheatreDto> createTheatre(@RequestBody CreateTheatreRequest req) {
        TheatreDto dto = adminService.createTheatre(req);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/screens")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScreenDto> createScreen(@RequestBody CreateScreenRequest req) {
        ScreenDto dto = adminService.createScreen(req);
        return ResponseEntity.status(201).body(dto);
    }

    // Shows
    @PostMapping("/shows")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowDto> createShow(@RequestBody CreateShowRequest req) {
        ShowDto dto = adminService.createShow(req);
        return ResponseEntity.status(201).body(dto);
    }

    @DeleteMapping("/shows/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShow(@PathVariable("id") Long id) {
        adminService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }
}
