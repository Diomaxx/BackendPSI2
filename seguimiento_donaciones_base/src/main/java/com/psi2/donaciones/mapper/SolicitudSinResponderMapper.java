package com.psi2.donaciones.mapper;

import com.psi2.donaciones.entities.entityMongo.SolicitudesSinResponder;
import com.psi2.donaciones.dto.SolicitudSinResponderDto;

public class SolicitudSinResponderMapper {

    public static SolicitudSinResponderDto toDto(SolicitudesSinResponder solicitud) {
        return new SolicitudSinResponderDto(
                solicitud.getId(),
                solicitud.getFechaSolicitud(),
                solicitud.getIdDestino(),
                solicitud.getCantidadPersonas(),
                solicitud.getIdSolicitante(),
                solicitud.getFechaInicioIncendio(),
                solicitud.getListaProductos(),
                solicitud.getCategoria()
        );
    }

    public static SolicitudesSinResponder toEntity(SolicitudSinResponderDto dto) {
        SolicitudesSinResponder solicitud = new SolicitudesSinResponder();
        solicitud.setId(dto.getId());
        solicitud.setFechaSolicitud(dto.getFechaSolicitud());
        solicitud.setIdDestino(dto.getIdDestino());
        solicitud.setCantidadPersonas(dto.getCantidadPersonas());
        solicitud.setIdSolicitante(dto.getIdSolicitante());
        solicitud.setFechaInicioIncendio(dto.getFechaInicioIncendio());
        solicitud.setListaProductos(dto.getListaProductos());
        solicitud.setCategoria(dto.getCategoria());
        return solicitud;
    }
}
