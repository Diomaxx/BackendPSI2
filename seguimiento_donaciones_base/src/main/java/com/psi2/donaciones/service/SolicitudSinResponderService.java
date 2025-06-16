package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.SolicitudSinResponderDto;
import com.psi2.donaciones.request.CrearSSDRequest;

import java.util.List;
import java.util.Map;

public interface SolicitudSinResponderService {
    List<SolicitudSinResponderDto> obtenerTodasSolicitudes();
    SolicitudSinResponderDto crearSolicitud(SolicitudSinResponderDto solicitudDto);
    boolean aprobarSolicitud(String idSolicitud, String ciEncargado);
    boolean rechazarSolicitud(String idSolicitud, String justificacion);
    boolean verificarDisponibilidad(List<String> listaProductos);
    Map<String, Integer> obtenerInventarioDisponible();
    SolicitudSinResponderDto crearSolicitudCompleta(CrearSSDRequest request);

}