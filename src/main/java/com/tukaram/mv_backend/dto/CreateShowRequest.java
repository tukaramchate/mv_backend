package com.tukaram.mv_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShowRequest {

    @NotNull
    private Long movieId;

    @NotNull
    private Long theatreId;

    @NotNull
    private Long screenId;

    @NotNull
    private LocalDateTime showTime;

    @NotNull
    @Positive
    private BigDecimal price;

    private String language;
}
