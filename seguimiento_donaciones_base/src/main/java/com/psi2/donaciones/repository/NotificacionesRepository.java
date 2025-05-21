package com.psi2.donaciones.repository;

import com.psi2.donaciones.entities.entityMongo.Notificaciones;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface NotificacionesRepository extends MongoRepository<Notificaciones,String> {
    long deleteByFechaCreacionBefore(Date fechaLimite);
}
