package com.psi2.donaciones.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DonanteDto {
    private Integer id_donante;
    private String nombres;
    private String apellido_paterno;
    private String apellido_materno;

}
