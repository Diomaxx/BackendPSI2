package com.psi2.donaciones.controller;

import com.psi2.donaciones.dto.SolicitudDonacionDto;
import com.psi2.donaciones.dto.SolicitudDto;
import com.psi2.donaciones.dto.SolicitudListaDto;
import com.psi2.donaciones.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin("*")
public class SolicitudController {
    @Autowired
    private SolicitudService solicitudService;


    @GetMapping
    public ResponseEntity<List<SolicitudDto>> getAllSolicitudes() {
        List<SolicitudDto> solicitudes = solicitudService.getAllSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/aprobadas/almacen")
    public ResponseEntity<List<SolicitudDonacionDto>> obtenerSolicitudesConDonacionesPendientes() {
        List<SolicitudDonacionDto> resultado = solicitudService.obtenerSolicitudesConDonacionesPendientes();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/resumen")
    public ResponseEntity<List<SolicitudListaDto>> obtenerResumenSolicitudes() {
        List<SolicitudListaDto> resultado = solicitudService.obtenerSolicitudesCompletas();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/aprobadas")
    public ResponseEntity<List<SolicitudDto>> obtenerSolicitudesAprobadas() {
        List<SolicitudDto> aprobadas = solicitudService.getSolicitudesAprobadas();
        return ResponseEntity.ok(aprobadas);
    }




}
