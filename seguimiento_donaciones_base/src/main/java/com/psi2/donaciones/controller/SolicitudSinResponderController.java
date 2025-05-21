package com.psi2.donaciones.controller;

import com.psi2.donaciones.dto.SolicitudSinResponderDto;
import com.psi2.donaciones.request.CrearSSDRequest;
import com.psi2.donaciones.service.SolicitudSinResponderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes-sin-responder")
@CrossOrigin("*")
public class SolicitudSinResponderController {

    @Autowired
    private SolicitudSinResponderService solicitudSinResponderService;



    @GetMapping
    public ResponseEntity<List<SolicitudSinResponderDto>> obtenerTodas() {
        List<SolicitudSinResponderDto> solicitudes = solicitudSinResponderService.obtenerTodasSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping("/crear-completa")
    public ResponseEntity<SolicitudSinResponderDto> crearSolicitudCompleta(@RequestBody CrearSSDRequest request) {
        SolicitudSinResponderDto resultado = solicitudSinResponderService.crearSolicitudCompleta(request);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<SolicitudSinResponderDto> crearSolicitud(@RequestBody SolicitudSinResponderDto solicitudDto) {
        try {
            SolicitudSinResponderDto creada = solicitudSinResponderService.crearSolicitud(solicitudDto);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @PostMapping("/aprobar/{idSolicitud}")
    public ResponseEntity<Boolean> aprobarSolicitud(@PathVariable String idSolicitud,@RequestBody String ciUsuario) {
        boolean resultado = solicitudSinResponderService.aprobarSolicitud(idSolicitud,ciUsuario);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/rechazar/{idSolicitud}")
    public ResponseEntity<Boolean> rechazarSolicitud(@PathVariable String idSolicitud,@RequestBody String justificacion) {
        boolean resultado = solicitudSinResponderService.rechazarSolicitud(idSolicitud,justificacion);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/inventario")
    public ResponseEntity<Map<String, Integer>> obtenerInventarioDisponible() {
        Map<String, Integer> disponibilidad = solicitudSinResponderService.obtenerInventarioDisponible();
        return ResponseEntity.ok(disponibilidad);
    }

    @PostMapping("/verificar")
    public ResponseEntity<Boolean> verificarDisponibilidad(@RequestBody List<String> listaProductos) {
        boolean disponible = solicitudSinResponderService.verificarDisponibilidad(listaProductos);
        return ResponseEntity.ok(disponible);
    }
} 