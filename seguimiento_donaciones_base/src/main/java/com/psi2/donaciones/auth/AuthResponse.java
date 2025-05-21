package com.psi2.donaciones.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private long expiration; // Timestamp en milisegundos

}
