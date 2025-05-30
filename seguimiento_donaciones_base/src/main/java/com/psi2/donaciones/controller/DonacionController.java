package com.psi2.donaciones.controller;

import com.psi2.donaciones.dto.AgradecimientoDto;
import com.psi2.donaciones.dto.DonacionDto;
import com.psi2.donaciones.dto.NewDonacionDto;
import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.request.ActualizarEntregaRequest;
import com.psi2.donaciones.service.DonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donaciones")
@CrossOrigin("*")
public class DonacionController {

    @Autowired
    private DonacionService donacionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @GetMapping
    public ResponseEntity<List<DonacionDto>> getAllDonaciones() {
        List<DonacionDto> donaciones = donacionService.getAllDonaciones();
        return ResponseEntity.ok(donaciones);
    }

    @GetMapping("/new")
    public ResponseEntity<List<NewDonacionDto>> getAllNewDonaciones() {
        List<NewDonacionDto> donaciones = donacionService.getAllNewDonaciones();
        return ResponseEntity.ok(donaciones);
    }

    @GetMapping("/donantes")
    public List<AgradecimientoDto> getDonacionesConDonantes() {
        return donacionService.obtenerDonacionesConDonantes();
    }



    @PostMapping("/armado/{idDonacion}")
    public ResponseEntity<?> avanzarArmadoPaquete(@PathVariable Integer idDonacion, @RequestBody ActualizarEntregaRequest request) {
        try {
            DonacionDto actualizada = donacionService.progresarEstadoArmadoPaquete(
                    idDonacion,
                    request.getCiUsuario(),
                    request.getImagen()
            );
            messagingTemplate.convertAndSend("/topic/paquete-donacion", actualizada);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/actualizar/{idDonacion}")
    public ResponseEntity<?> actualizarEntrega(@PathVariable Integer idDonacion, @RequestBody ActualizarEntregaRequest request) {
        try {
            System.out.println("Estado recibido: '" + request.getEstado() + "'");

            DonacionDto actualizada = donacionService.actualizarEntregaDonacion(
                    idDonacion,
                    request.getCiUsuario(),
                    request.getEstado(),
                    request.getImagen(),
                    request.getLatitud(),
                    request.getLongitud()
            );
            messagingTemplate.convertAndSend("/topic/donacion-actualizada", actualizada);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            // Devuelve 400 Bad Request con mensaje personalizado
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/total")
    public ResponseEntity<Long> obtenerMetricasGenerales() {
        return ResponseEntity.ok(donacionService.contarTotalDonaciones());
    }
}
