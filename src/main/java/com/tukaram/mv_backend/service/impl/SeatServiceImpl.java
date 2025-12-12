package com.tukaram.mv_backend.service.impl;

import com.tukaram.mv_backend.dto.CreateSeatRequest;
import com.tukaram.mv_backend.dto.SeatDto;
import com.tukaram.mv_backend.dto.SeatMapper;
import com.tukaram.mv_backend.exception.ResourceNotFoundException;
import com.tukaram.mv_backend.model.Seat;
import com.tukaram.mv_backend.model.Screen;
import com.tukaram.mv_backend.repository.SeatRepository;
import com.tukaram.mv_backend.repository.ScreenRepository;
import com.tukaram.mv_backend.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;

    @Override
    @Transactional
    public SeatDto createSeat(CreateSeatRequest request) {
        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + request.getScreenId()));

        if (seatRepository.existsByScreenIdAndSeatNumber(screen.getId(), request.getSeatNumber())) {
            throw new IllegalArgumentException("Seat number already exists in this screen");
        }

        Seat seat = SeatMapper.fromCreateRequest(request);
        seat.setScreen(screen);
        Seat saved = seatRepository.save(seat);
        return SeatMapper.toDto(saved);
    }

    @Override
    @Transactional
    public List<SeatDto> createSeatsBulk(List<CreateSeatRequest> requests) {
        if (requests == null || requests.isEmpty()) return List.of();
        Long screenId = requests.get(0).getScreenId();
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + screenId));

        List<Seat> seats = requests.stream().map(r -> {
            if (!r.getScreenId().equals(screenId)) {
                throw new IllegalArgumentException("All seats must belong to same screen");
            }
            if (seatRepository.existsByScreenIdAndSeatNumber(screenId, r.getSeatNumber())) {
                throw new IllegalArgumentException("Duplicate seat number: " + r.getSeatNumber());
            }
            Seat s = SeatMapper.fromCreateRequest(r);
            s.setScreen(screen);
            return s;
        }).collect(Collectors.toList());

        List<Seat> saved = seatRepository.saveAll(seats);
        return saved.stream().map(SeatMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SeatDto updateSeat(Long id, CreateSeatRequest request) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + id));

        // if screen change requested
        if (request.getScreenId() != null && !request.getScreenId().equals(seat.getScreen().getId())) {
            Screen newScreen = screenRepository.findById(request.getScreenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + request.getScreenId()));
            seat.setScreen(newScreen);
        }

        SeatMapper.updateEntityFromRequest(seat, request);
        seatRepository.save(seat);
        return SeatMapper.toDto(seat);
    }

    @Override
    @Transactional
    public void deleteSeat(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seat not found with id: " + id);
        }
        seatRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SeatDto getById(Long id) {
        return seatRepository.findById(id)
                .map(SeatMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatDto> getByScreenId(Long screenId) {
        return seatRepository.findByScreenId(screenId).stream()
                .map(SeatMapper::toDto)
                .collect(Collectors.toList());
    }
}
