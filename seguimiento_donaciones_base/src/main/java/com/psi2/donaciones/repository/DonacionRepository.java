package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entitySQL.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonacionRepository extends JpaRepository<Donacion, Integer> {
    long countByFechaEntregaIsNotNull();
    List<Donacion> findBySolicitud_IdSolicitud(Integer idSolicitud);
    long countByFechaEntregaIsNull();
    List<Donacion> findByFechaEntregaIsNotNull();
}
