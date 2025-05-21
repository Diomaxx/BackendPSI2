package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entitySQL.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCi(String ci);
    boolean existsByCi(String ci);

}
