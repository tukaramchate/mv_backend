package com.tukaram.mv_backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheatreDto {
    private Long id;
    private String name;
    private String location;
}
