package com.tukaram.mv_backend.service.impl;

import com.tukaram.mv_backend.dto.CreateShowRequest;
import com.tukaram.mv_backend.dto.ShowDto;
import com.tukaram.mv_backend.dto.ShowMapper;
import com.tukaram.mv_backend.exception.ResourceNotFoundException;
import com.tukaram.mv_backend.model.*;
import com.tukaram.mv_backend.repository.*;
import com.tukaram.mv_backend.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;

    @Override
    public ShowDto createShow(CreateShowRequest req) {

        Movie movie = movieRepository.findById(req.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Theatre theatre = theatreRepository.findById(req.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        Screen screen = screenRepository.findById(req.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        Show show = Show.builder()
                .movie(movie)
                .theatre(theatre)
                .screen(screen)
                .showTime(req.getShowTime())
                .price(req.getPrice())
                .language(req.getLanguage())
                .available(true)
                .build();

        return ShowMapper.toDto(showRepository.save(show));
    }

    @Override
    public ShowDto updateShow(Long id, CreateShowRequest req) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        show.setShowTime(req.getShowTime());
        show.setPrice(req.getPrice());
        show.setLanguage(req.getLanguage());

        return ShowMapper.toDto(showRepository.save(show));
    }

    @Override
    public void deleteShow(Long id) {
        if (!showRepository.existsById(id)) {
            throw new ResourceNotFoundException("Show not found");
        }
        showRepository.deleteById(id);
    }

    @Override
    public ShowDto getShowById(Long id) {
        return showRepository.findById(id)
                .map(ShowMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
    }

    @Override
    public List<ShowDto> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieId(movieId)
                .stream().map(ShowMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ShowDto> getShowsByTheatre(Long theatreId) {
        return showRepository.findByTheatreId(theatreId)
                .stream().map(ShowMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ShowDto> getShowsByScreen(Long screenId) {
        return showRepository.findByScreenId(screenId)
                .stream().map(ShowMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ShowDto> getAllShows() {
        return showRepository.findAll()
                .stream().map(ShowMapper::toDto).collect(Collectors.toList());
    }
}
