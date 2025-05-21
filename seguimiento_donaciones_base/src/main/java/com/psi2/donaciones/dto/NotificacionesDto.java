package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionesDto {
    private String id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String nivelSeveridad;

    @Indexed(expireAfterSeconds = 10)
    private Date fechaCreacion;
}

