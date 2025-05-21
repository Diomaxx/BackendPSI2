package com.psi2.donaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoReservadoDto extends ProductoDto {
    private Integer cantidadReservada;
    
    public ProductoReservadoDto(ProductoDto producto, Integer cantidadReservada) {
        super(producto.getId_articulo(), producto.getNombre_articulo(), producto.getTotal_restante(),producto.getMedida_abreviada());
        this.cantidadReservada = cantidadReservada;
    }
    
    public Integer getCantidadDisponibleReal() {
        return getTotal_restante() - cantidadReservada;
    }
} 