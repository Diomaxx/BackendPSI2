package com.psi2.donaciones.mapper;

import com.psi2.donaciones.entities.entityMongo.Metricas;
import com.psi2.donaciones.dto.MetricasDto;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MetricasMapper {

    public static MetricasDto toDto(Metricas metricas) {
        if (metricas == null) {
            return null;
        }

        MetricasDto dto = new MetricasDto();

        dto.setId(metricas.getId());
        dto.setTotalSolicitudesRecibidas(metricas.getTotalSolicitudesRecibidas());
        dto.setSolicitudesAprobadas(metricas.getSolicitudesAprobadas());
        dto.setSolicitudesRechazadas(metricas.getSolicitudesRechazadas());
        dto.setDonacionesEntregadas(metricas.getDonacionesEntregadas());
        dto.setDonacionesPendientes(metricas.getDonacionesPendientes());
        dto.setTiempoPromedioRespuesta(metricas.getTiempoPromedioRespuesta());
        dto.setTiempoPromedioEntrega(metricas.getTiempoPromedioEntrega());
        dto.setSolicitudesSinResponder(metricas.getSolicitudesSinResponder());
        dto.setFechaCreacion(metricas.getFechaCreacion());

        dto.setTopProductosMasSolicitados(metricas.getTopProductosMasSolicitados() != null ?
                metricas.getTopProductosMasSolicitados() : new HashMap<>());

        dto.setSolicitudesPorProvincia(metricas.getSolicitudesPorProvincia() != null ?
                metricas.getSolicitudesPorProvincia() : new HashMap<>());

        dto.setSolicitudesPorMes(metricas.getSolicitudesPorMes() != null ?
                metricas.getSolicitudesPorMes() : new HashMap<>());

        dto.setSolicitudesPorCategoria(metricas.getSolicitudesPorCategoria() != null ?
                metricas.getSolicitudesPorCategoria() : new HashMap<>());
        dto.setDonEntregadasProvincia(metricas.getDonEntregadasProvincia() != null ?
                metricas.getDonEntregadasProvincia() : new HashMap<>());

        if (metricas.getSolicitantesMasActivos() != null) {
            List<MetricasDto.SolicitanteActivoDto> activos = metricas.getSolicitantesMasActivos()
                    .stream()
                    .map(sa -> new MetricasDto.SolicitanteActivoDto(sa.getNombre(), sa.getCantidad()))
                    .collect(Collectors.toList());
            dto.setSolicitantesMasActivos(activos);
        } else {
            dto.setSolicitantesMasActivos(new ArrayList<>());
        }

        return dto;
    }

    public static Metricas toEntity(MetricasDto dto) {
        if (dto == null) {
            return null;
        }

        Metricas metricas = new Metricas();

        metricas.setId(dto.getId());
        metricas.setTotalSolicitudesRecibidas(dto.getTotalSolicitudesRecibidas());
        metricas.setSolicitudesAprobadas(dto.getSolicitudesAprobadas());
        metricas.setSolicitudesRechazadas(dto.getSolicitudesRechazadas());
        metricas.setDonacionesEntregadas(dto.getDonacionesEntregadas());
        metricas.setDonacionesPendientes(dto.getDonacionesPendientes());
        metricas.setTiempoPromedioRespuesta(dto.getTiempoPromedioRespuesta());
        metricas.setTiempoPromedioEntrega(dto.getTiempoPromedioEntrega());
        metricas.setSolicitudesSinResponder(dto.getSolicitudesSinResponder());
        metricas.setFechaCreacion(dto.getFechaCreacion());

        metricas.setTopProductosMasSolicitados(dto.getTopProductosMasSolicitados() != null ?
                dto.getTopProductosMasSolicitados() : new HashMap<>());

        metricas.setSolicitudesPorProvincia(dto.getSolicitudesPorProvincia() != null ?
                dto.getSolicitudesPorProvincia() : new HashMap<>());

        metricas.setSolicitudesPorMes(dto.getSolicitudesPorMes() != null ?
                dto.getSolicitudesPorMes() : new HashMap<>());

        metricas.setSolicitudesPorCategoria(dto.getSolicitudesPorCategoria() != null ?
                dto.getSolicitudesPorCategoria() : new HashMap<>());

        metricas.setDonEntregadasProvincia(dto.getDonEntregadasProvincia() != null ?
                dto.getDonEntregadasProvincia() : new HashMap<>());

        if (dto.getSolicitantesMasActivos() != null) {
            List<Metricas.SolicitanteActivo> activos = dto.getSolicitantesMasActivos()
                    .stream()
                    .map(saDto -> new Metricas.SolicitanteActivo(saDto.getNombre(), saDto.getCantidad()))
                    .collect(Collectors.toList());
            metricas.setSolicitantesMasActivos(activos);
        } else {
            metricas.setSolicitantesMasActivos(new ArrayList<>());
        }

        return metricas;
    }
}