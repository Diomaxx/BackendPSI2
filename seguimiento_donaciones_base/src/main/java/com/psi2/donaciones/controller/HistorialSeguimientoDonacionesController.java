package com.psi2.donaciones.controller;

import com.psi2.donaciones.dto.HistorialSeguimientoDonacionesDto;
import com.psi2.donaciones.service.HistorialSeguimientoDonacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
@CrossOrigin("*")
public class HistorialSeguimientoDonacionesController {
    @Autowired
    private HistorialSeguimientoDonacionesService historialSeguimientoDonacionesService;

    @GetMapping("list")
    public ResponseEntity<List<HistorialSeguimientoDonacionesDto>> listSolicitud() {
        List<HistorialSeguimientoDonacionesDto> historial = historialSeguimientoDonacionesService.getAllHistorial();
        return new ResponseEntity<>(historial, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialSeguimientoDonacionesDto> getHistorialById(@PathVariable Integer id) {
        HistorialSeguimientoDonacionesDto dto = historialSeguimientoDonacionesService.getHistorialById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/donacion/{donacionId}")
    public ResponseEntity<List<HistorialSeguimientoDonacionesDto>> getHistorialByDonacion(@PathVariable Integer donacionId) {
        List<HistorialSeguimientoDonacionesDto> historial = historialSeguimientoDonacionesService.getHistorialByDonacionId(donacionId);
        return new ResponseEntity<>(historial, HttpStatus.OK);
    }

}