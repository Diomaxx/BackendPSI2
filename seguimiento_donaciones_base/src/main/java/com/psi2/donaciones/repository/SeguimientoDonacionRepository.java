package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entityMongo.SeguimientoDonacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SeguimientoDonacionRepository extends MongoRepository<SeguimientoDonacion, String> {
}
