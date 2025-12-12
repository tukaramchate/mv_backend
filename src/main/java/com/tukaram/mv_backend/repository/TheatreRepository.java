package com.tukaram.mv_backend.repository;

import com.tukaram.mv_backend.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    // add custom queries later if needed (e.g., findByLocation)
}
