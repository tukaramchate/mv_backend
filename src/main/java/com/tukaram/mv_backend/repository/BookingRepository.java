package com.tukaram.mv_backend.repository;

import com.tukaram.mv_backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByShowId(Long showId);

    List<Booking> findByUserId(Long userId);
}
