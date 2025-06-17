package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudConPersonalDto {
    private int idSolicitud;
    private Date fechaInicioIncendio;
    private Date fechaSolicitud;
    private int cantidadPersonas;
    private String categoria;
    private String listaProductos;
    private int idSolicitante;
    private int idDestino;
    private Boolean apoyoaceptado;
    private Map<String, Integer> personalNecesario;
}
