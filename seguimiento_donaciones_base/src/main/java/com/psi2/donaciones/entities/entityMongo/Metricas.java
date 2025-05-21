package com.psi2.donaciones.entities.entityMongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "metricas")
public class Metricas {
    @Id
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
    private List<SolicitanteActivo> solicitantesMasActivos;
    private Map<String, Integer> solicitudesPorCategoria;
    @CreatedDate
    private LocalDateTime fechaCreacion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvinciaCount {
        private String provincia;
        private Integer cantidad;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolicitanteActivo {
        private String nombre;
        private Integer cantidad;
    }

}