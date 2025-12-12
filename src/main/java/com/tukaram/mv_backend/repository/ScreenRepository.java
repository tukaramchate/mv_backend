package com.tukaram.mv_backend.repository;

import com.tukaram.mv_backend.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findByTheatreId(Long theatreId);
    boolean existsByTheatreIdAndScreenName(Long theatreId, String screenName);
}
