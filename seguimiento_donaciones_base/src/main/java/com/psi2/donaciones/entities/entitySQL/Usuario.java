package com.psi2.donaciones.entities.entitySQL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String ci;

    @JsonIgnore
    private String contrasena;

    @Column(unique = true)
    private String correoElectronico;

    private Boolean admin;
    private Boolean active;

}
