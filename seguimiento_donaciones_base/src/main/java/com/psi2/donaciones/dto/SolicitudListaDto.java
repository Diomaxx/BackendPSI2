package com.psi2.donaciones.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class SolicitudListaDto {
    private String idSolicitud;
    private Date fechaInicioIncendio;
    private Date fechaSolicitud;
    private Boolean aprobada;
    private Integer cantidadPersonas;
    private String justificacion;
    private String categoria;
    private String productos;
    private SolicitanteDto solicitante;
    private DestinoDto destino;

    public SolicitudListaDto(String idSolicitud, Date fechaInicioIncendio, Date fechaSolicitud, Boolean aprobada,Integer cantidadPersonas, String justificacion, String categoria, String listaProductos, SolicitanteDto solicitanteDto, DestinoDto destinoDto) {
        this.idSolicitud = idSolicitud;
        this.fechaInicioIncendio = fechaInicioIncendio;
        this.fechaSolicitud = fechaSolicitud;
        this.aprobada = aprobada;
        this.justificacion = justificacion;
        this.categoria = categoria;
        this.productos = listaProductos;
        this.solicitante = solicitanteDto;
        this.destino = destinoDto;
        this.cantidadPersonas = cantidadPersonas;
    }
}
