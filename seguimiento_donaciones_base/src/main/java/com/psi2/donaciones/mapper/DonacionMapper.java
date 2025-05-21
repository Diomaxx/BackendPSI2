package com.psi2.donaciones.mapper;

import com.psi2.donaciones.dto.DonacionDto;
import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.entities.entitySQL.Usuario;
import com.psi2.donaciones.entities.entitySQL.Solicitud;

public class DonacionMapper {

    public static DonacionDto toDto(Donacion donacion) {
        DonacionDto dto = new DonacionDto();
        dto.setIdDonacion(donacion.getIdDonacion());
        dto.setCodigo(donacion.getCodigo());
        dto.setFechaAprobacion(donacion.getFechaAprobacion());
        dto.setCategoria(donacion.getCategoria());
        dto.setFechaEntrega(donacion.getFechaEntrega());
        dto.setImagen(donacion.getImagen());

        if (donacion.getEncargado() != null) {
            dto.setEncargado(donacion.getEncargado());
        }

        if (donacion.getSolicitud() != null) {
            dto.setSolicitud(donacion.getSolicitud());
        }

        return dto;
    }

    public static Donacion toEntity(DonacionDto dto, Usuario encargado, Solicitud solicitud) {
        Donacion donacion = new Donacion();
        donacion.setIdDonacion(dto.getIdDonacion());
        donacion.setCodigo(dto.getCodigo());
        donacion.setFechaAprobacion(dto.getFechaAprobacion());
        donacion.setFechaEntrega(dto.getFechaEntrega());
        donacion.setCategoria(dto.getCategoria());
        donacion.setImagen(dto.getImagen());
        donacion.setEncargado(encargado);
        donacion.setSolicitud(solicitud);
        return donacion;
    }
}
