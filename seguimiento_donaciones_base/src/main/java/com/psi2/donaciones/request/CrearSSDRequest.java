package com.psi2.donaciones.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
public class CrearSSDRequest {

    private String provincia;
    private String comunidad;
    private String direccion;
    private Double latitud;
    private Double longitud;

    private String nombreSolicitante;
    private String apellidoSolicitante;
    private String ciSolicitante;
    private String telefonoSolicitante;

    private Date fechaInicioIncendio;
    private List<String> listaProductos;
    private Integer cantidadPersonas;
    private String categoria;

}
