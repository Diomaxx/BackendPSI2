package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DestinoDto {
    private Integer id;
    private String direccion;
    private String provincia;
    private String comunidad;
    private Double latitud;
    private Double longitud;
}
