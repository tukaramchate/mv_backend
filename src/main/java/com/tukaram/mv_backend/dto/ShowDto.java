package com.tukaram.mv_backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowDto {

    private Long id;
    private Long movieId;
    private Long theatreId;
    private Long screenId;

    private LocalDateTime showTime;
    private BigDecimal price;
    private String language;

    private boolean available;
}
