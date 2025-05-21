package com.psi2.donaciones.mapper;

import com.psi2.donaciones.dto.UbicacionDto;
import com.psi2.donaciones.entities.entitySQL.Ubicacion;

public class UbicacionMapper {

    public static UbicacionDto toDto(Ubicacion ubicacion) {
        return new UbicacionDto(
                ubicacion.getIdUbicacion(),
                ubicacion.getLatitud(),
                ubicacion.getLongitud(),
                ubicacion.getZona()
        );
    }

    public static Ubicacion toEntity(UbicacionDto dto) {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setIdUbicacion(dto.getIdUbicacion());
        ubicacion.setLatitud(dto.getLatitud());
        ubicacion.setLongitud(dto.getLongitud());
        ubicacion.setZona(dto.getZona());
        return ubicacion;
    }
}
