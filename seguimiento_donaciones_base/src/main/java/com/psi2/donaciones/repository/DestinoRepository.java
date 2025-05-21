package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entitySQL.Destino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinoRepository extends JpaRepository<Destino, Integer> {
}
