package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.CreateTheatreRequest;
import com.tukaram.mv_backend.dto.TheatreDto;

import java.util.List;

public interface TheatreService {
    TheatreDto createTheatre(CreateTheatreRequest request);
    TheatreDto updateTheatre(Long id, CreateTheatreRequest request);
    void deleteTheatre(Long id);
    TheatreDto getById(Long id);
    List<TheatreDto> getAll();
}
