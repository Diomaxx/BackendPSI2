package com.psi2.donaciones.auth;


import lombok.Data;

@Data
public class AuthRequest {
    private String cedulaIdentidad;
    private String contrasena;
}