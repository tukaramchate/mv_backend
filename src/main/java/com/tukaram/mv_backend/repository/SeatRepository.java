package com.tukaram.mv_backend.repository;

import com.tukaram.mv_backend.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreenId(Long screenId);
    boolean existsByScreenIdAndSeatNumber(Long screenId, String seatNumber);
    List<Seat> findByIdIn(List<Long> ids);
}
