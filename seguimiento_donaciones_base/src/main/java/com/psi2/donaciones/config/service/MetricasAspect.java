package com.psi2.donaciones.config.service;

import com.psi2.donaciones.dto.MetricasDto;
import com.psi2.donaciones.service.MetricasService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Aspect
@Component
public class MetricasAspect {

    @Autowired
    private MetricasService metricasService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @AfterReturning("within(@org.springframework.web.bind.annotation.RestController *) && " +
            "(@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping)) && " +
            "!within(com.psi2.donaciones.controller.AuthController) && " +
            "!within(com.psi2.donaciones.controller.UsuarioController)")
    public void afterPostOrPutMethods() {
        try {
            MetricasDto nuevaMetrica = metricasService.obtenerMetricas();
            messagingTemplate.convertAndSend("/topic/nueva-metrica", nuevaMetrica);
            System.out.println("Métrica enviada tras POST o PUT");
        } catch (Exception e) {
            System.err.println("⚠Error al generar/enviar métrica automática: " + e.getMessage());
        }
    }
}
