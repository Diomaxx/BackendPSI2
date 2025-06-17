package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteCompletoHistorialDto {
    private Integer idDonacion;
    private String codigoDonacion;
    private Date fechaAprobacion;
    private Date fechaEntrega;
    private String categoriaDonacion;
    
    private String ciEncargado;
    private String nombreEncargado;
    private String emailEncargado;
    private String telefonoEncargado;
    
    private Integer idSolicitud;
    private Date fechaInicioIncendio;
    private Date fechaSolicitud;
    private Boolean solicitudAprobada;
    private Integer cantidadPersonas;
    private String justificacion;
    private String categoriaSolicitud;
    private String listaProductos;
    
    private String ciSolicitante;
    private String nombreSolicitante;
    private String apellidoSolicitante;
    private String telefonoSolicitante;
    private String emailSolicitante;
    
    private String comunidadDestino;
    private String direccionDestino;
    private String provinciaDestino;
    private Double latitudDestino;
    private Double longitudDestino;
    
    private String estadoActual;
    private Double latitudActual;
    private Double longitudActual;
    private java.util.Date timestampActual;
    
    private List<PuntoHistorialDto> puntosHistorial;
    
    private Integer totalPuntosHistorial;
    private Double distanciaRecorrida;
    private Long tiempoTotalDias;
    
    private LocalDateTime fechaGeneracionReporte;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PuntoHistorialDto {
        private Integer idHistorial;
        private String ciUsuario;
        private String estado;
        private Timestamp fechaActualizacion;
        private Double latitud;
        private Double longitud;
        
    }
} 