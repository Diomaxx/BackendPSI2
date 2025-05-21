package com.psi2.donaciones.mapper;

import com.psi2.donaciones.dto.SolicitudDto;
import com.psi2.donaciones.entities.entitySQL.Solicitud;
import com.psi2.donaciones.entities.entitySQL.Solicitante;
import com.psi2.donaciones.entities.entitySQL.Destino;

public class SolicitudMapper {

    public static SolicitudDto toDto(Solicitud solicitud) {
        SolicitudDto dto = new SolicitudDto();
        dto.setIdSolicitud(solicitud.getIdSolicitud());
        dto.setFechaInicioIncendio(solicitud.getFechaInicioIncendio());
        dto.setFechaSolicitud(solicitud.getFechaSolicitud());
        dto.setAprobada(solicitud.getAprobada());
        dto.setCantidadPersonas(solicitud.getCantidadPersonas());
        dto.setJustificacion(solicitud.getJustificacion());
        dto.setCategoria(solicitud.getCategoria());
        dto.setListaProductos(solicitud.getListaProductos());

        if (solicitud.getSolicitante() != null) {
            dto.setIdSolicitante(solicitud.getSolicitante().getIdSolicitante());
        }

        if (solicitud.getDestino() != null) {
            dto.setIdDestino(solicitud.getDestino().getIdDestino());
        }

        return dto;
    }

    public static Solicitud toEntity(SolicitudDto dto, Solicitante solicitante, Destino destino) {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(dto.getIdSolicitud());
        solicitud.setFechaInicioIncendio(dto.getFechaInicioIncendio());
        solicitud.setFechaSolicitud(dto.getFechaSolicitud());
        solicitud.setAprobada(dto.getAprobada());
        solicitud.setCantidadPersonas(dto.getCantidadPersonas());
        solicitud.setJustificacion(dto.getJustificacion());
        solicitud.setCategoria(dto.getCategoria());
        solicitud.setListaProductos(dto.getListaProductos());
        solicitud.setSolicitante(solicitante);
        solicitud.setDestino(destino);
        return solicitud;
    }
}
