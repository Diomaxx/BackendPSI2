package com.psi2.donaciones.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistroGlobalDto {
    private String nombre;
    private String apellido;
    private String email;
    private String ci;
    private String password;
    private String telefono;

}
