package com.threadverse_backend.service.auth;

import com.threadverse_backend.dto.request.LoginRequest;
import com.threadverse_backend.dto.request.RegisterRequest;
import com.threadverse_backend.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}