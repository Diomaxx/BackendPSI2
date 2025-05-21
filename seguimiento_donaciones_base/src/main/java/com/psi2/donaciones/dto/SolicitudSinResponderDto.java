package com.psi2.donaciones.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudSinResponderDto {
    private String id;
    private Date fechaInicioIncendio;
    private Date fechaSolicitud;
    private List<String> listaProductos;
    private Integer cantidadPersonas;
    private String categoria;
    private String idSolicitante;
    private String idDestino;

    public SolicitudSinResponderDto(String id, Date fechaSolicitud, String idDestino, Integer cantidadPersonas, String idSolicitante, Date fechaInicioIncendio, List<String> listaProductos, String categoria) {
        this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.idDestino = idDestino;
        this.cantidadPersonas = cantidadPersonas;
        this.idSolicitante = idSolicitante;
        this.fechaInicioIncendio = fechaInicioIncendio;
        this.listaProductos = listaProductos;
        this.categoria = categoria;
    }

}
