package com.psi2.donaciones.dto;

import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.entities.entitySQL.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialSeguimientoDonacionesDto {
    private Integer idHistorial;
    private Donacion donacion;
    private String ciUsuario;
    private String estado;
    private String imagenEvidencia;
    private java.sql.Timestamp fechaActualizacion;
    private Ubicacion ubicacion;
    private Double latitud;
    private Double longitud;

}