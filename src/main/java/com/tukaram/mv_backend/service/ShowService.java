package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.CreateShowRequest;
import com.tukaram.mv_backend.dto.ShowDto;

import java.util.List;

public interface ShowService {

    ShowDto createShow(CreateShowRequest request);

    ShowDto updateShow(Long id, CreateShowRequest request);

    void deleteShow(Long id);

    ShowDto getShowById(Long id);

    List<ShowDto> getShowsByMovie(Long movieId);

    List<ShowDto> getShowsByTheatre(Long theatreId);

    List<ShowDto> getShowsByScreen(Long screenId);

    List<ShowDto> getAllShows();
}
