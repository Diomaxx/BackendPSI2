package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.NotificacionesDto;

import java.util.List;

public interface NotificacionesService {
    List<NotificacionesDto> obtenerNotificaciones();
    List<NotificacionesDto> obtenerNotificacionesSolicitud();
    List<NotificacionesDto> obtenerNotificacionesAlerta();
}
