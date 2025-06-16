package com.psi2.donaciones.service;


import com.psi2.donaciones.dto.MetricasDto;

import java.util.List;

public interface MetricasService {
    MetricasDto obtenerMetricas();
    MetricasDto mostrarMetricas();
    double calcularTiempoPromedioAprobacion();
}