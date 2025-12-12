package com.tukaram.mv_backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreenDto {
    private Long id;
    private Long theatreId;
    private String screenName;
    private Integer totalSeats;
    private String seatMap;
}
