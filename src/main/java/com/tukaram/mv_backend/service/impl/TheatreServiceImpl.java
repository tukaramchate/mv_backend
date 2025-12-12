package com.tukaram.mv_backend.service.impl;

import com.tukaram.mv_backend.dto.CreateTheatreRequest;
import com.tukaram.mv_backend.dto.TheatreDto;
import com.tukaram.mv_backend.dto.TheatreMapper;
import com.tukaram.mv_backend.exception.ResourceNotFoundException;
import com.tukaram.mv_backend.model.Theatre;
import com.tukaram.mv_backend.repository.TheatreRepository;
import com.tukaram.mv_backend.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;

    @Override
    @Transactional
    public TheatreDto createTheatre(CreateTheatreRequest request) {
        Theatre t = TheatreMapper.fromCreateRequest(request);
        Theatre saved = theatreRepository.save(t);
        return TheatreMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TheatreDto updateTheatre(Long id, CreateTheatreRequest request) {
        Theatre t = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
        TheatreMapper.updateEntityFromRequest(t, request);
        theatreRepository.save(t);
        return TheatreMapper.toDto(t);
    }

    @Override
    @Transactional
    public void deleteTheatre(Long id) {
        if (!theatreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theatre not found with id: " + id);
        }
        theatreRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TheatreDto getById(Long id) {
        return theatreRepository.findById(id)
                .map(TheatreMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TheatreDto> getAll() {
        return theatreRepository.findAll().stream()
                .map(TheatreMapper::toDto)
                .collect(Collectors.toList());
    }
}
