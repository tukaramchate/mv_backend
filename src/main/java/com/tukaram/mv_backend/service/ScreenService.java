package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.CreateScreenRequest;
import com.tukaram.mv_backend.dto.ScreenDto;

import java.util.List;

public interface ScreenService {
    ScreenDto createScreen(CreateScreenRequest request);
    ScreenDto updateScreen(Long id, CreateScreenRequest request);
    void deleteScreen(Long id);
    ScreenDto getById(Long id);
    List<ScreenDto> getAllByTheatre(Long theatreId);
    List<ScreenDto> getAllScreens();
}
