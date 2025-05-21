package com.psi2.donaciones.entities.entityMongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notificaciones")
public class Notificaciones {
    @Id
    private String id;

    private String titulo;
    private String descripcion;
    private String tipo;
    private String nivelSeveridad;
    private Date fechaCreacion;
}

