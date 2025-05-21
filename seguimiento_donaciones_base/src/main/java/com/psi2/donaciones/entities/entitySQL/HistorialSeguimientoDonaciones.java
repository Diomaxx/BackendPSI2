package com.psi2.donaciones.entities.entitySQL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Historial_Seguimiento_Donaciones")
public class HistorialSeguimientoDonaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorial;

    @ManyToOne
    @JoinColumn(name = "idDonacion")
    private Donacion donacion;

    @Column(name = "ci_usuario")
    private String ciUsuario;

    private String estado;
    private String imagenEvidencia;
    private java.sql.Timestamp fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "idUbicacion")
    private Ubicacion ubicacion;

}
