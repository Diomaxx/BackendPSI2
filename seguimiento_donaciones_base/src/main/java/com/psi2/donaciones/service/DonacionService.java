package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.DonacionDto;
import com.psi2.donaciones.dto.NewDonacionDto;

import java.util.List;

public interface DonacionService {
    List<DonacionDto> getAllDonaciones();
    List<NewDonacionDto> getAllNewDonaciones();
    DonacionDto actualizarEntregaDonacion(Integer idDonacion, String nuevaCi, String estado, String imagen, Double latitud, Double longitud);
    long contarTotalDonaciones();
    void crearDonacionDesdeSolicitud(Integer idSolicitud, String ciEncargado);
    double calcularTiempoPromedioEntrega();
    DonacionDto progresarEstadoArmadoPaquete(Integer idDonacion, String ciEncargado, String imagen);

}
