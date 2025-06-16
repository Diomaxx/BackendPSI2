package com.psi2.donaciones.controller;


import com.psi2.donaciones.dto.SeguimientoCompletoDto;
import com.psi2.donaciones.dto.SeguimientoDonacionDto;
import com.psi2.donaciones.exception.ResourceNotFoundException;
import com.psi2.donaciones.service.SeguimientoDonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seguimientodonaciones")
@CrossOrigin("*")
public class SeguimientoDonacionController {
    @Autowired
    private SeguimientoDonacionService seguimientoDonacionService;
    @GetMapping("/contar-entregadas")
    public ResponseEntity<Long> contarEntregadas() {
        return ResponseEntity.ok(seguimientoDonacionService.contarDonacionesEntregadas());

    }
    @GetMapping
    public ResponseEntity<List<SeguimientoDonacionDto>> getAllSeguimientos() {
        List<SeguimientoDonacionDto> result = seguimientoDonacionService.getAllSeguimientos();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/completos")
    public ResponseEntity<List<SeguimientoCompletoDto>> obtenerTodosSeguimientos() {
        List<SeguimientoCompletoDto> seguimientos = seguimientoDonacionService.obtenerTodosSeguimientosConHistorial();
        return ResponseEntity.ok(seguimientos);
    }



    @GetMapping("/por-donacion/{idDonacion}")
    public ResponseEntity<SeguimientoDonacionDto> getSeguimientoPorDonacion(
            @PathVariable Integer idDonacion) {
        return ResponseEntity.ok(seguimientoDonacionService.buscarPorIdDonacion(idDonacion));
    }


}
