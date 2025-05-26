package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDonacionDto {
    private Integer idDonacion;
    private String codigo;
    private Date fecha_pedido;
    private String descripcion;
    private String ubicacion;
    private Double latitud_destino;
    private Double longitud_destino;
}

