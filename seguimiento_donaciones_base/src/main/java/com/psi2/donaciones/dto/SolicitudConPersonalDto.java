package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudConPersonalDto {
    private int idSolicitud;
    private Date fechaInicioIncendio;
    private Date fechaSolicitud;
    private boolean aprobada;
    private int cantidadPersonas;
    private String justificacion;
    private String categoria;
    private String listaProductos;
    private int idSolicitante;
    private int idDestino;
    private List<String> personalNecesario;
}
