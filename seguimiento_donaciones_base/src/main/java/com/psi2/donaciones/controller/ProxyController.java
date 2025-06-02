package com.psi2.donaciones.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @GetMapping("/inventario")
    public ResponseEntity<String> obtenerInventario() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "https://backenddonaciones.onrender.com/api/inventario/stock", String.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al contactar con el inventario externo");
        }
    }

    @PostMapping("/ruta")
    public ResponseEntity<?> getRuta(@RequestBody Map<String, Object> body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "5b3ce3597851110001cf6248099d6d212bcf4f3c9a99b05ca49ae755");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openrouteservice.org/v2/directions/driving-car/geojson",
                    request,
                    String.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al solicitar ruta: " + e.getMessage());
        }
    }
}
