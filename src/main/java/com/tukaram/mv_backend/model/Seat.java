package com.tukaram.mv_backend.model;

import com.tukaram.mv_backend.model.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"screen_id", "seat_number"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many seats belong to one screen
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;   // e.g., "A1"

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", length = 20)
    private SeatType seatType;   // e.g., REGULAR, PREMIUM

    // Optional coordinates / metadata
    private Integer rowNumber;
    private Integer colNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
