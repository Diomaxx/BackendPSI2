package com.psi2.donaciones.entities.entityMongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "seguimiento_donaciones")
public class SeguimientoDonacion {
    @Id
    private String id;

    private String idDonacion;
    private String ciUsuario;
    private String estado;
    private String imagenEvidencia;
    private Double latitud;
    private Double longitud;
    private Date timestamp;

}
