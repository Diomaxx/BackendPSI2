package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoCompletoDto {
    private String id;
    private String idDonacion;
    private String codigo;
    private String ciUsuario;
    private String estado;
    private String imagenEvidencia;
    private Double latitud;
    private Double longitud;
    private Date timestamp;
    private String origen;
    private String destino;
    private Double latitud_destino;
    private Double longitud_destino;

    private List<PuntoHistorialDto> historial;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PuntoHistorialDto {
        private Double latitud;
        private Double longitud;
    }

}
