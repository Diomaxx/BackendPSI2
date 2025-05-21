package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.ProductoDto;

import java.util.List;
import java.util.Map;

public interface InventarioExternoService {
    // Consultar el inventario desde la API externa
    List<ProductoDto> consultarInventario();
    
    // Consultar un producto específico
    ProductoDto consultarProducto(String idProducto);
    
    // Descontar productos del inventario
    boolean descontarProductos(Map<String, Integer> productos);
    void verificarStockBajo();
} 