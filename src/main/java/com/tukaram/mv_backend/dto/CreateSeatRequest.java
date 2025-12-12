package com.tukaram.mv_backend.dto;

import com.tukaram.mv_backend.model.enums.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSeatRequest {

    @NotNull
    private Long screenId;

    @NotBlank
    private String seatNumber;

    @NotNull
    private SeatType seatType;

    private Integer rowNumber;
    private Integer colNumber;
}
