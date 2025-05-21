package com.psi2.donaciones.mapper;

import com.psi2.donaciones.dto.NotificacionesDto;
import com.psi2.donaciones.entities.entityMongo.Notificaciones;

public class NotificacionesMapper {

    public static NotificacionesDto toDto(Notificaciones notificacion) {
        if (notificacion == null) {
            return null;
        }

        NotificacionesDto dto = new NotificacionesDto();
        dto.setId(notificacion.getId());
        dto.setTitulo(notificacion.getTitulo());
        dto.setDescripcion(notificacion.getDescripcion());
        dto.setTipo(notificacion.getTipo());
        dto.setNivelSeveridad(notificacion.getNivelSeveridad());
        dto.setFechaCreacion(notificacion.getFechaCreacion());

        return dto;
    }

    public static Notificaciones toEntity(NotificacionesDto dto) {
        if (dto == null) {
            return null;
        }

        Notificaciones notificacion = new Notificaciones();
        notificacion.setId(dto.getId());
        notificacion.setTitulo(dto.getTitulo());
        notificacion.setDescripcion(dto.getDescripcion());
        notificacion.setTipo(dto.getTipo());
        notificacion.setNivelSeveridad(dto.getNivelSeveridad());
        notificacion.setFechaCreacion(dto.getFechaCreacion());

        return notificacion;
    }
}
