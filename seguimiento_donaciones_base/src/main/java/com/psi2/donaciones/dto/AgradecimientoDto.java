package com.psi2.donaciones.dto;

import com.psi2.donaciones.entities.entitySQL.Solicitud;
import com.psi2.donaciones.entities.entitySQL.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgradecimientoDto {
    private Integer idDonacion;
    private String codigo;
    private Date fechaEntrega;
    private String imagen;
    private List<DonanteDto> donantes;
}
