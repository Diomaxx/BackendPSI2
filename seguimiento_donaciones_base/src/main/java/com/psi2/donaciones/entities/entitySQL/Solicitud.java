package com.psi2.donaciones.entities.entitySQL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Solicitud")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSolicitud;

    private java.sql.Date fechaInicioIncendio;
    private java.sql.Date fechaSolicitud;
    private Boolean aprobada;
    private Integer cantidadPersonas;
    private String justificacion;
    private String categoria;
    private String listaProductos;

    @ManyToOne
    @JoinColumn(name = "idSolicitante")
    private Solicitante solicitante;

    @ManyToOne
    @JoinColumn(name = "idDestino")
    private Destino destino;

}
