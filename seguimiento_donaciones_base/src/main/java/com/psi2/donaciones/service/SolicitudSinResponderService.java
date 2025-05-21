package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.SolicitudSinResponderDto;
import com.psi2.donaciones.request.CrearSSDRequest;

import java.util.List;
import java.util.Map;

public interface SolicitudSinResponderService {
    // Obtener todas las solicitudes sin responder
    List<SolicitudSinResponderDto> obtenerTodasSolicitudes();
    
    // Crear una solicitud sin responder
    SolicitudSinResponderDto crearSolicitud(SolicitudSinResponderDto solicitudDto);
    
    // Aprobar una solicitud sin responder (crea la solicitud SQL y la donación)
    boolean aprobarSolicitud(String idSolicitud, String ciEncargado);
    
    // Rechazar una solicitud sin responder (crea la solicitud SQL rechazada)
    boolean rechazarSolicitud(String idSolicitud, String justificacion);
    
    // Verificar disponibilidad para una nueva solicitud
    boolean verificarDisponibilidad(List<String> listaProductos);
    
    // Obtener inventario disponible considerando reservas
    Map<String, Integer> obtenerInventarioDisponible();
    SolicitudSinResponderDto crearSolicitudCompleta(CrearSSDRequest request);

}