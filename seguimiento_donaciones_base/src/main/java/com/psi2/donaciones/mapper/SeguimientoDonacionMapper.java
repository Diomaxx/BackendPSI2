package com.psi2.donaciones.mapper;

import com.psi2.donaciones.dto.SeguimientoDonacionDto;
import com.psi2.donaciones.entities.entityMongo.SeguimientoDonacion;

public class SeguimientoDonacionMapper {

    public static SeguimientoDonacionDto toDto(SeguimientoDonacion seguimiento) {
        return new SeguimientoDonacionDto(
                seguimiento.getId(),
                seguimiento.getIdDonacion(),
                seguimiento.getCiUsuario(),
                seguimiento.getEstado(),
                seguimiento.getImagenEvidencia(),
                seguimiento.getLatitud(),
                seguimiento.getLongitud(),
                seguimiento.getTimestamp()
        );
    }

    public static SeguimientoDonacion toEntity(SeguimientoDonacionDto dto) {
        SeguimientoDonacion seguimiento = new SeguimientoDonacion();
        seguimiento.setId(dto.getId());
        seguimiento.setIdDonacion(dto.getIdDonacion());
        seguimiento.setCiUsuario(dto.getCiUsuario());
        seguimiento.setEstado(dto.getEstado());
        seguimiento.setImagenEvidencia(dto.getImagenEvidencia());
        seguimiento.setLatitud(dto.getLatitud());
        seguimiento.setLongitud(dto.getLongitud());
        seguimiento.setTimestamp(dto.getTimestamp());
        return seguimiento;
    }
}
