package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.DonanteDto;
import com.psi2.donaciones.dto.ProductoDto;

import java.util.List;
import java.util.Map;

public interface InventarioExternoService {
    List<ProductoDto> consultarInventario();
    ProductoDto consultarProducto(String idProducto);
    void verificarStockBajo();
    List<DonanteDto> obtenerDonantesPorCodigo(String codigoDonacion);
} 