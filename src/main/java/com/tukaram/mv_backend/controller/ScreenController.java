package com.tukaram.mv_backend.controller;

import com.tukaram.mv_backend.dto.CreateScreenRequest;
import com.tukaram.mv_backend.dto.ScreenDto;
import com.tukaram.mv_backend.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @GetMapping
    public ResponseEntity<List<ScreenDto>> getAll(@RequestParam(value = "theatreId", required = false) Long theatreId) {
        List<ScreenDto> result = (theatreId == null) ? screenService.getAllScreens() : screenService.getAllByTheatre(theatreId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreenDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(screenService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ScreenDto> create(@Valid @RequestBody CreateScreenRequest request) {
        ScreenDto created = screenService.createScreen(request);
        return ResponseEntity.created(URI.create("/api/screens/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScreenDto> update(@PathVariable Long id, @Valid @RequestBody CreateScreenRequest request) {
        return ResponseEntity.ok(screenService.updateScreen(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        screenService.deleteScreen(id);
        return ResponseEntity.noContent().build();
    }
}
