package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entityMongo.Metricas;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MetricasRepository extends MongoRepository<Metricas,String> {
    Optional<Metricas> findTopByOrderByFechaCreacionDesc();
}