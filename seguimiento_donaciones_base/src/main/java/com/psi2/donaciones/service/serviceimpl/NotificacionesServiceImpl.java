package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.NotificacionesDto;
import com.psi2.donaciones.dto.SolicitudDto;
import com.psi2.donaciones.entities.entityMongo.Notificaciones;
import com.psi2.donaciones.entities.entitySQL.Solicitud;
import com.psi2.donaciones.mapper.NotificacionesMapper;
import com.psi2.donaciones.mapper.SolicitudMapper;
import com.psi2.donaciones.repository.NotificacionesRepository;
import com.psi2.donaciones.service.NotificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionesServiceImpl implements NotificacionesService {
    @Autowired
    NotificacionesRepository notificacionesRepository;

    @Override
    public List<NotificacionesDto> obtenerNotificaciones() {
        List<Notificaciones> notificaciones = notificacionesRepository.findAll();
        return notificaciones.stream()
                .map(NotificacionesMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificacionesDto> obtenerNotificacionesSolicitud() {
        return notificacionesRepository.findAll().stream()
                .filter(n -> !"Nueva Solicitud ingresada".equalsIgnoreCase(n.getTitulo()))
                .filter(n -> n.getTipo().toLowerCase().contains("solicitud"))
                .map(NotificacionesMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificacionesDto> obtenerNotificacionesAlerta() {
        return notificacionesRepository.findAll().stream()
                .filter(n -> !"Nueva Solicitud ingresada".equalsIgnoreCase(n.getTitulo()))
                .filter(n -> !n.getTipo().toLowerCase().contains("alerta"))
                .map(NotificacionesMapper::toDto)
                .collect(Collectors.toList());
    }
}
