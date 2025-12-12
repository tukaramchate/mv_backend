package com.tukaram.mv_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booked_seats",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"show_id", "seat_id"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // back-ref to booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // show for quick queries
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    // optional: price at time of booking
    @Column(name = "price", precision = 12, scale = 2)
    private java.math.BigDecimal price;
}
