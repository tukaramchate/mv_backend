package com.tukaram.mv_backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private String language;
    private String genre;
    private String posterUrl;
}
