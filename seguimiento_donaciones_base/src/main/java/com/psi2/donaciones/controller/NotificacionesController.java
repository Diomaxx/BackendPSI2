package com.psi2.donaciones.controller;

import com.psi2.donaciones.dto.NotificacionesDto;
import com.psi2.donaciones.dto.SolicitudDto;
import com.psi2.donaciones.service.NotificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin("*")
public class NotificacionesController {
    @Autowired
    private NotificacionesService notificacionesService;
    @GetMapping
    public ResponseEntity<List<NotificacionesDto>> obtenerNotificaciones() {
        List<NotificacionesDto> notificaciones = notificacionesService.obtenerNotificaciones();
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/almacen-solicitudes")
    public ResponseEntity<List<NotificacionesDto>> obtenerNotificacionesDeSolicitud() {
        List<NotificacionesDto> solicitudes = notificacionesService.obtenerNotificacionesSolicitud();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/almacen-alertas")
    public ResponseEntity<List<NotificacionesDto>> obtenerNotificacionesDeAlerta() {
        List<NotificacionesDto> alertas = notificacionesService.obtenerNotificacionesAlerta();
        return ResponseEntity.ok(alertas);
    }
}
