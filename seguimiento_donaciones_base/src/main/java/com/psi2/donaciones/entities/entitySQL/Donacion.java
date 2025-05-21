package com.psi2.donaciones.entities.entitySQL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Donacion")
public class Donacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDonacion;

    private String codigo;
    private java.sql.Date fechaAprobacion;
    private java.sql.Date fechaEntrega;
    private String imagen;
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "idEncargado")
    private Usuario encargado;

    @ManyToOne
    @JoinColumn(name = "idSolicitud")
    private Solicitud solicitud;

}
