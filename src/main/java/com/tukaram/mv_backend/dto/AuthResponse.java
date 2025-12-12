package com.tukaram.mv_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String tokenType; // "Bearer"
    private Long userId;
    private String email;
    private String name;
}
