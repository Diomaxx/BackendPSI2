package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entitySQL.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {
}