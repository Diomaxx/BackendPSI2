package com.psi2.donaciones.mapper;

import com.psi2.donaciones.dto.HistorialSeguimientoDonacionesDto;
import com.psi2.donaciones.entities.entitySQL.HistorialSeguimientoDonaciones;
import com.psi2.donaciones.entities.entitySQL.Ubicacion;

public class HistorialSeguimientoDonacionesMapper {
    public static HistorialSeguimientoDonacionesDto toDto(HistorialSeguimientoDonaciones historialSeguimientoDonaciones) {
        Ubicacion ubicacion = historialSeguimientoDonaciones.getUbicacion();
        Double latitud = ubicacion != null ? ubicacion.getLatitud() : null;
        Double longitud = ubicacion != null ? ubicacion.getLongitud() : null;

        return new HistorialSeguimientoDonacionesDto(
                historialSeguimientoDonaciones.getIdHistorial(),
                historialSeguimientoDonaciones.getDonacion(),
                historialSeguimientoDonaciones.getCiUsuario(),
                historialSeguimientoDonaciones.getEstado(),
                historialSeguimientoDonaciones.getImagenEvidencia(),
                historialSeguimientoDonaciones.getFechaActualizacion(),
                ubicacion,
                latitud,
                longitud
        );
    }

    public static HistorialSeguimientoDonaciones toEntity(HistorialSeguimientoDonacionesDto dto) {
        HistorialSeguimientoDonaciones historialSeguimientoDonaciones = new HistorialSeguimientoDonaciones();
        historialSeguimientoDonaciones.setIdHistorial(dto.getIdHistorial());
        historialSeguimientoDonaciones.setDonacion(dto.getDonacion());
        historialSeguimientoDonaciones.setCiUsuario(dto.getCiUsuario());
        historialSeguimientoDonaciones.setEstado(dto.getEstado());
        historialSeguimientoDonaciones.setImagenEvidencia(dto.getImagenEvidencia());
        historialSeguimientoDonaciones.setFechaActualizacion(dto.getFechaActualizacion());
        historialSeguimientoDonaciones.setUbicacion(dto.getUbicacion());
        return historialSeguimientoDonaciones;
    }
}