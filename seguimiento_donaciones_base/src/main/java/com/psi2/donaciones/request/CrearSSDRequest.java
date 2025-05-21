package com.psi2.donaciones.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
public class CrearSSDRequest {

    // Datos del destino
    private String provincia;
    private String comunidad;
    private String direccion;
    private Double latitud;
    private Double longitud;

    // Datos del solicitante
    private String nombreSolicitante;
    private String apellidoSolicitante;
    private String ciSolicitante;
    private String telefonoSolicitante;

    // Datos de la solicitud sin responder
    private Date fechaInicioIncendio;
    private List<String> listaProductos;
    private Integer cantidadPersonas;
    private String categoria;

}
