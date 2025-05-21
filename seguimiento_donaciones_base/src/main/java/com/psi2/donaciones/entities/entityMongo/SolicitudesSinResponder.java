package com.psi2.donaciones.entities.entityMongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "solicitudes_sin_responder")
public class SolicitudesSinResponder {
    @Id
    private String id;

    @Field("fechaInicioIncendio")
    private Date fechaInicioIncendio;

    @Field("fechaSolicitud")
    private Date fechaSolicitud;

    @Field("listaProductos")
    private List<String> listaProductos;

    @Field("cantidadPersonas")
    private Integer cantidadPersonas;

    @Field("categoria")
    private String categoria;

    @Field("idSolicitante")
    private String idSolicitante;

    @Field("idDestino")
    private String idDestino;

}
