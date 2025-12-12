package com.tukaram.mv_backend.service.impl;

import com.tukaram.mv_backend.dto.CreateScreenRequest;
import com.tukaram.mv_backend.dto.ScreenDto;
import com.tukaram.mv_backend.dto.ScreenMapper;
import com.tukaram.mv_backend.exception.ResourceNotFoundException;
import com.tukaram.mv_backend.model.Screen;
import com.tukaram.mv_backend.model.Theatre;
import com.tukaram.mv_backend.repository.ScreenRepository;
import com.tukaram.mv_backend.repository.TheatreRepository;
import com.tukaram.mv_backend.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;

    @Override
    @Transactional
    public ScreenDto createScreen(CreateScreenRequest request) {
        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + request.getTheatreId()));

        // prevent duplicate screen names in same theatre
        if (screenRepository.existsByTheatreIdAndScreenName(theatre.getId(), request.getScreenName())) {
            throw new IllegalArgumentException("Screen with this name already exists in the theatre");
        }

        Screen s = ScreenMapper.fromCreateRequest(request);
        s.setTheatre(theatre);
        Screen saved = screenRepository.save(s);
        return ScreenMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ScreenDto updateScreen(Long id, CreateScreenRequest request) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + id));

        // if theatreId provided and different, verify theatre exists
        if (request.getTheatreId() != null && !request.getTheatreId().equals(screen.getTheatre().getId())) {
            Theatre newTheatre = theatreRepository.findById(request.getTheatreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + request.getTheatreId()));
            screen.setTheatre(newTheatre);
        }

        ScreenMapper.updateEntityFromRequest(screen, request);
        screenRepository.save(screen);
        return ScreenMapper.toDto(screen);
    }

    @Override
    @Transactional
    public void deleteScreen(Long id) {
        if (!screenRepository.existsById(id)) {
            throw new ResourceNotFoundException("Screen not found with id: " + id);
        }
        screenRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ScreenDto getById(Long id) {
        return screenRepository.findById(id)
                .map(ScreenMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreenDto> getAllByTheatre(Long theatreId) {
        return screenRepository.findByTheatreId(theatreId).stream()
                .map(ScreenMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreenDto> getAllScreens() {
        return screenRepository.findAll().stream()
                .map(ScreenMapper::toDto)
                .collect(Collectors.toList());
    }
}
