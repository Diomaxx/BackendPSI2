package com.psi2.donaciones.controller;

import com.psi2.donaciones.dto.SolicitudSinResponderDto;
import com.psi2.donaciones.service.MetricasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.psi2.donaciones.dto.MetricasDto;
import com.psi2.donaciones.dto.SolicitudDto;
import com.psi2.donaciones.service.MetricasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metricas")
@CrossOrigin("*")
public class MetricasController {
    @Autowired
    private MetricasService metricasService;

    @PostMapping("add")
    public ResponseEntity<MetricasDto> addResumenGeneral() {
        MetricasDto result = metricasService.obtenerMetricas();
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<MetricasDto> mostrarMetricas() {
        MetricasDto metricas = metricasService.mostrarMetricas();
        return ResponseEntity.ok(metricas);
    }
}