package com.tukaram.mv_backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedSeatDto {
    private Long id;
    private Long seatId;
    private Long showId;
    private Long bookingId;
    private String seatNumber;
}
