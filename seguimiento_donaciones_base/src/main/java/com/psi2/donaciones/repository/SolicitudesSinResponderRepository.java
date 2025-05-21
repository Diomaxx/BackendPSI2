package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entityMongo.SolicitudesSinResponder;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SolicitudesSinResponderRepository extends MongoRepository<SolicitudesSinResponder, String> {
    @Query(value = "{ $or: [ { aprobada: null }, { aprobada: { $exists: false } } ] }", count = true)
    long countSolicitudesSinResponder();
}