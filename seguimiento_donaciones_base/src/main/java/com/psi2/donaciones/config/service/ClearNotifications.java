package com.psi2.donaciones.config.service;

import com.psi2.donaciones.repository.NotificacionesRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.util.Date;

@Service
public class ClearNotifications {

    @Autowired
    private NotificacionesRepository notificacionRepository;

    /*@Scheduled(fixedRate = 10000) // cada 10 segundos
    public void borrarNotificacionesViejas() {
        Instant hace1Minuto = Instant.now().minusSeconds(60);
        Date fechaLimite = Date.from(hace1Minuto);

        long eliminadas = notificacionRepository.deleteByFechaCreacionBefore(fechaLimite);
        System.out.println("Borradas: " + eliminadas);
    }*/

    @Scheduled(cron = "0 0 3 * * *")
    public void borrarNotificacionesViejas() {
        Instant dosSemanasAtras = Instant.now().minusSeconds(14 * 24 * 60 * 60);
        Date fechaLimite = Date.from(dosSemanasAtras);

        long eliminadas = notificacionRepository.deleteByFechaCreacionBefore(fechaLimite);
        System.out.println("Notificaciones eliminadas: " + eliminadas);
    }

}
