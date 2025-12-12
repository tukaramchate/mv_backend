package com.tukaram.mv_backend.repository;

import com.tukaram.mv_backend.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    long countByMovieId(Long movieId);

    List<Show> findByMovieId(Long movieId);

    List<Show> findByTheatreId(Long theatreId);

    List<Show> findByScreenId(Long screenId);

    List<Show> findByShowTimeBetween(LocalDateTime from, LocalDateTime to);


}
