package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.HistorialSeguimientoDonacionesDto;
import com.psi2.donaciones.dto.ReporteCompletoHistorialDto;
import com.psi2.donaciones.entities.entitySQL.Donacion;

import java.util.List;

public interface HistorialSeguimientoDonacionesService {
    List<HistorialSeguimientoDonacionesDto> getAllHistorial();
    HistorialSeguimientoDonacionesDto getHistorialById(Integer id);
    List<HistorialSeguimientoDonacionesDto> getHistorialByDonacionId(Integer donacionId);
    void registrarHistorialSeguimiento(Donacion donacion, String ciUsuario, String estado, String imagen, Double latitud, Double longitud);
    ReporteCompletoHistorialDto generarReporteCompletoHistorial(Integer donacionId);

}