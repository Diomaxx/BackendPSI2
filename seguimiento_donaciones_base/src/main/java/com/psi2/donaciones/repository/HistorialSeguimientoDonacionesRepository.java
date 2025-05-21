package com.psi2.donaciones.repository;

import com.psi2.donaciones.dto.HistorialSeguimientoDonacionesDto;
import com.psi2.donaciones.entities.entitySQL.HistorialSeguimientoDonaciones;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialSeguimientoDonacionesRepository extends JpaRepository<HistorialSeguimientoDonaciones, Integer> {
    List<HistorialSeguimientoDonaciones> findByDonacion_IdDonacion(Integer idDonacion);


}