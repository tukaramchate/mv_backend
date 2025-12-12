package com.tukaram.mv_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTheatreRequest {
    @NotBlank
    private String name;
    private String location;
}
