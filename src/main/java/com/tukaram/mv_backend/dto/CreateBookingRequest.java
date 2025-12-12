package com.tukaram.mv_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingRequest {

    @NotNull
    private Long showId;

    // optional user id
    private Long userId;

    @NotEmpty
    private List<Long> seatIds;

    // optional payment info string for mock
    private String paymentInfo;
}
