package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entitySQL.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Integer> {
}
