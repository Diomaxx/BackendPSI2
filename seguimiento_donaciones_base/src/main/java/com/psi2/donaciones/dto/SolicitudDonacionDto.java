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
    private Integer idSolicitud;
    private Date fechaInicioIncendio;
    private Date fechaSolicitud;
    private Boolean aprobada;
    private Integer cantidadPersonas;
    private String justificacion;
    private String categoria;
    private String listaProductos;
    private Integer idDonacion;

}

