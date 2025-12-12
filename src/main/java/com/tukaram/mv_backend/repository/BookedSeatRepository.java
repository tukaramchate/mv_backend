package com.tukaram.mv_backend.repository;

import com.tukaram.mv_backend.model.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {
    boolean existsByShowIdAndSeatId(Long showId, Long seatId);

    // checks if any of seatIds are already booked for a show
    boolean existsByShowIdAndSeatIdIn(Long showId, List<Long> seatIds);

    List<BookedSeat> findByShowIdAndSeatIdIn(Long showId, List<Long> seatIds);
}
