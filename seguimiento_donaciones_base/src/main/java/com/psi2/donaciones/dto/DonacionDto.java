package com.psi2.donaciones.dto;

import com.psi2.donaciones.entities.entitySQL.Solicitud;
import com.psi2.donaciones.entities.entitySQL.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonacionDto {
    private Integer idDonacion;
    private String codigo;
    private Date fechaAprobacion;
    private Date fechaEntrega;
    private String categoria;
    private String imagen;
    private Usuario encargado;
    private Solicitud solicitud;

}

