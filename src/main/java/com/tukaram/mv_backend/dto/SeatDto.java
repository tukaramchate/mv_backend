package com.tukaram.mv_backend.dto;

import com.tukaram.mv_backend.model.enums.SeatType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDto {
    private Long id;
    private Long screenId;
    private String seatNumber;
    private SeatType seatType;
    private Integer rowNumber;
    private Integer colNumber;
}
