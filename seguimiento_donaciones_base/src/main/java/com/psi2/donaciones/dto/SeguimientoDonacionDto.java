package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoDonacionDto {
    private String id;
    private String idDonacion;
    private String ciUsuario;
    private String estado;
    private String imagenEvidencia;
    private Double latitud;
    private Double longitud;
    private Date timestamp;
}
