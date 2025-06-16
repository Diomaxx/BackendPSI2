package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.SeguimientoCompletoDto;
import com.psi2.donaciones.dto.SeguimientoDonacionDto;

import java.util.List;

public interface SeguimientoDonacionService {
    List<SeguimientoCompletoDto> obtenerTodosSeguimientosConHistorial();
    List<SeguimientoDonacionDto> getAllSeguimientos();
    SeguimientoDonacionDto buscarPorIdDonacion(Integer donacionId);
    void deleteSeguimientoByDonacionId(Integer donacionId);
    long contarDonacionesEntregadas();
}

