package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.SeguimientoCompletoDto;
import com.psi2.donaciones.dto.SeguimientoDonacionDto;

import java.util.List;

public interface SeguimientoDonacionService {
    List<SeguimientoCompletoDto> obtenerTodosSeguimientosConHistorial();
    List<SeguimientoDonacionDto> getAllSeguimientos();
    //SeguimientoDonacionDto actualizarEstadoSeguimiento(String donacionId, String nuevoEstado, String imagen);
    SeguimientoDonacionDto buscarPorIdDonacion(Integer donacionId);
    //SeguimientoDonacionDto crearSeguimientoInicial(String donacionId, String estado);
    void deleteSeguimientoByDonacionId(Integer donacionId);
    long contarDonacionesEntregadas();
}

