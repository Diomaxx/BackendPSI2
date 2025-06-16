package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.DonanteDto;
import com.psi2.donaciones.dto.NotificacionesDto;
import com.psi2.donaciones.dto.ProductoDto;
import com.psi2.donaciones.entities.entityMongo.Notificaciones;
import com.psi2.donaciones.mapper.NotificacionesMapper;
import com.psi2.donaciones.repository.NotificacionesRepository;
import com.psi2.donaciones.service.InventarioExternoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InventarioExternoServiceImpl implements InventarioExternoService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private NotificacionesRepository notificacionesRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    private String inventarioApiUrl = "https://backenddonaciones.onrender.com/api";

    @Override
    public List<ProductoDto> consultarInventario() {
        ResponseEntity<ProductoDto[]> response = 
                restTemplate.getForEntity(inventarioApiUrl + "/inventario/stock", ProductoDto[].class);
        
        return Arrays.asList(response.getBody());
    }

    @Override
    public ProductoDto consultarProducto(String idProducto) {
        ResponseEntity<ProductoDto> response =
                restTemplate.getForEntity(inventarioApiUrl + "/inventario/stock/articulo/" + idProducto, ProductoDto.class);

        System.out.println("Producto Consultado: " + response.getBody());
        return response.getBody();
    }


    public void verificarStockBajo() {
        List<ProductoDto> productos = this.consultarInventario();

        for (ProductoDto producto : productos) {
            if (producto.getTotal_restante() < 20) {
                String nombre = producto.getNombre_articulo();
                int cantidad = producto.getTotal_restante();

                NotificacionesDto dto = new NotificacionesDto();
                dto.setTitulo("Cantidad baja de producto");
                dto.setDescripcion("El producto \"" + nombre + "\" tiene un stock actual de " + cantidad + ", inferior al mínimo recomendado.");
                dto.setTipo("Alerta");
                dto.setNivelSeveridad("Media");
                dto.setFechaCreacion(new Date());

                messagingTemplate.convertAndSend("/topic/nueva-notificacion", dto);

                Notificaciones notificacion = NotificacionesMapper.toEntity(dto);
                notificacionesRepository.save(notificacion);

                System.out.println("Notificación guardada para: " + nombre);
            }
        }
    }

    @Override
    public List<DonanteDto> obtenerDonantesPorCodigo(String codigoDonacion) {
        String url = inventarioApiUrl + "/paquetes/donantes/" + codigoDonacion;

        ResponseEntity<List<DonanteDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DonanteDto>>() {}
        );

        return response.getBody();
    }





} 