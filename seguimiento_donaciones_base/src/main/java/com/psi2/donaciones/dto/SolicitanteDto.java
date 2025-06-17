package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitanteDto {
    private Integer id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String ci;
    private String email;
}
