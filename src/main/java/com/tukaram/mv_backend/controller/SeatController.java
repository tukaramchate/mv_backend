package com.tukaram.mv_backend.controller;

import com.tukaram.mv_backend.dto.CreateSeatRequest;
import com.tukaram.mv_backend.dto.SeatDto;
import com.tukaram.mv_backend.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<List<SeatDto>> getByScreen(@RequestParam(value = "screenId", required = false) Long screenId) {
        if (screenId == null) {
            return ResponseEntity.ok(List.of()); // or return all seats if desired
        }
        return ResponseEntity.ok(seatService.getByScreenId(screenId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SeatDto> create(@Valid @RequestBody CreateSeatRequest request) {
        SeatDto created = seatService.createSeat(request);
        return ResponseEntity.created(URI.create("/api/seats/" + created.getId())).body(created);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<SeatDto>> createBulk(@Valid @RequestBody List<CreateSeatRequest> requests) {
        List<SeatDto> created = seatService.createSeatsBulk(requests);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDto> update(@PathVariable Long id, @Valid @RequestBody CreateSeatRequest request) {
        return ResponseEntity.ok(seatService.updateSeat(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
