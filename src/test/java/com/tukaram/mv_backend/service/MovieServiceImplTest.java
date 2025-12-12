package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.CreateMovieRequest;
import com.tukaram.mv_backend.dto.MovieDto;
import com.tukaram.mv_backend.exception.ResourceNotFoundException;
import com.tukaram.mv_backend.model.Movie;
import com.tukaram.mv_backend.repository.MovieRepository;
import com.tukaram.mv_backend.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMovie_shouldReturnDto() {
        CreateMovieRequest req = CreateMovieRequest.builder()
                .title("Test Movie")
                .durationMinutes(100)
                .description("Desc")
                .language("English")
                .genre("Action")
                .posterUrl("http://poster")
                .build();

        Movie saved = Movie.builder()
                .id(1L)
                .title(req.getTitle())
                .durationMinutes(req.getDurationMinutes())
                .description(req.getDescription())
                .language(req.getLanguage())
                .genre(req.getGenre())
                .posterUrl(req.getPosterUrl())
                .build();

        when(movieRepository.save(any(Movie.class))).thenReturn(saved);

        MovieDto dto = movieService.createMovie(req);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Movie", dto.getTitle());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void getMovieById_whenNotFound_shouldThrow() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> movieService.getMovieById(1L));
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void getAllMovies_shouldReturnList() {
        Movie m1 = Movie.builder().id(1L).title("A").durationMinutes(90).build();
        Movie m2 = Movie.builder().id(2L).title("B").durationMinutes(110).build();
        when(movieRepository.findAll()).thenReturn(List.of(m1, m2));

        List<MovieDto> list = movieService.getAllMovies();

        assertEquals(2, list.size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void updateMovie_shouldUpdateAndReturn() {
        CreateMovieRequest req = CreateMovieRequest.builder()
                .title("Updated")
                .durationMinutes(120)
                .build();

        Movie existing = Movie.builder().id(1L).title("Old").durationMinutes(90).build();

        when(movieRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(movieRepository.save(existing)).thenReturn(existing);

        MovieDto dto = movieService.updateMovie(1L, req);

        assertEquals("Updated", dto.getTitle());
        assertEquals(120, dto.getDurationMinutes());
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).save(existing);
    }

    @Test
    void deleteMovie_whenNotExists_shouldThrow() {
        when(movieRepository.existsById(5L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> movieService.deleteMovie(5L));
        verify(movieRepository, times(1)).existsById(5L);
    }
}
