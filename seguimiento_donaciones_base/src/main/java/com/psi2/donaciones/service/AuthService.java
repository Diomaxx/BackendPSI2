package com.psi2.donaciones.service;

import com.psi2.donaciones.auth.AuthRequest;
import com.psi2.donaciones.auth.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
