package com.tukaram.mv_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "screens", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"theatre_id", "screen_name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many screens belong to one theatre
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", nullable = false)
    private Theatre theatre;

    @Column(name = "screen_name", nullable = false)
    private String screenName;

    @Column(name = "total_seats")
    private Integer totalSeats;

    // optional JSON layout (store as text)
    @Column(name = "seat_map", columnDefinition = "TEXT")
    private String seatMap;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // relationship to Seat (not fetched eagerly)
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
