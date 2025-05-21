package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entitySQL.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
    long countByAprobadaFalse();
    List<Solicitud> findByIdSolicitudIn(Collection<String> ids);

}
