package com.psi2.donaciones.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEntregaRequest {
    private String ciUsuario;
    private String estado;
    private String imagen;
    private Double Latitud;
    private Double Longitud;
}
