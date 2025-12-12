package com.tukaram.mv_backend.service;

import com.tukaram.mv_backend.dto.AuthResponse;
import com.tukaram.mv_backend.dto.LoginRequest;
import com.tukaram.mv_backend.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
