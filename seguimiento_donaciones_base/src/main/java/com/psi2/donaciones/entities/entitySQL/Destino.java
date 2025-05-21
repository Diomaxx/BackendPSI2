package com.psi2.donaciones.entities.entitySQL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Destino")
public class Destino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDestino;

    private String provincia;
    private String comunidad;
    private String direccion;
    private Double latitud;
    private Double longitud;

}
