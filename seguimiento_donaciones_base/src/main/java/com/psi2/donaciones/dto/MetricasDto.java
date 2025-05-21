package com.psi2.donaciones.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetricasDto {
    private String id;
    private Integer totalSolicitudesRecibidas;
    private Integer solicitudesAprobadas;
    private Integer solicitudesRechazadas;
    private Integer donacionesEntregadas;
    private Integer donacionesPendientes;
    private String tiempoPromedioRespuesta;
    private Map<String, Integer> donEntregadasProvincia;
    private Integer solicitudesSinResponder;
    private Map<String, Integer> solicitudesPorMes;

    private Map<String,Integer> topProductosMasSolicitados;
    private String tiempoPromedioEntrega;
    private Map<String, Integer> solicitudesPorProvincia;
    private List<SolicitanteActivoDto> solicitantesMasActivos;
    private Map<String, Integer> solicitudesPorCategoria;
    private LocalDateTime fechaCreacion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolicitanteActivoDto {
        private String nombre;
        private Integer cantidad;

    }

}