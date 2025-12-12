package com.tukaram.mv_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateScreenRequest {

    @NotNull
    private Long theatreId;

    @NotBlank
    private String screenName;

    @NotNull
    private Integer totalSeats;

    // optional JSON layout string
    private String seatMap;
}
