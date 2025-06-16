package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.HistorialSeguimientoDonacionesDto;
import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.entities.entitySQL.HistorialSeguimientoDonaciones;
import com.psi2.donaciones.entities.entitySQL.Ubicacion;
import com.psi2.donaciones.mapper.HistorialSeguimientoDonacionesMapper;
import com.psi2.donaciones.repository.HistorialSeguimientoDonacionesRepository;
import com.psi2.donaciones.repository.UbicacionRepository;
import com.psi2.donaciones.service.HistorialSeguimientoDonacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialSeguimientoDonacionesServiceImpl implements HistorialSeguimientoDonacionesService {

    @Autowired
    private HistorialSeguimientoDonacionesRepository historialSeguimientoDonacionesRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Override
    public List<HistorialSeguimientoDonacionesDto> getAllHistorial() {
        List<HistorialSeguimientoDonaciones> historialSeguimientoDonaciones = historialSeguimientoDonacionesRepository.findAll();
        return historialSeguimientoDonaciones.stream()
                .map(HistorialSeguimientoDonacionesMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public HistorialSeguimientoDonacionesDto getHistorialById(Integer id) {
        HistorialSeguimientoDonaciones historial = historialSeguimientoDonacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial not found with ID: " + id));
        return HistorialSeguimientoDonacionesMapper.toDto(historial);
    }

    @Override
    public List<HistorialSeguimientoDonacionesDto> getHistorialByDonacionId(Integer donacionId) {
        List<HistorialSeguimientoDonaciones> historialList = historialSeguimientoDonacionesRepository.findByDonacion_IdDonacion(donacionId);
        return historialList.stream()
                .map(HistorialSeguimientoDonacionesMapper::toDto)
                .collect(Collectors.toList());
    }

    public void registrarHistorialSeguimiento(Donacion donacion, String ciUsuario, String estado, String imagen, Double latitud, Double longitud) {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(latitud);
        ubicacion.setLongitud(longitud);
        Ubicacion ubicacionGuardada = ubicacionRepository.save(ubicacion);

        HistorialSeguimientoDonaciones historial = new HistorialSeguimientoDonaciones();
        historial.setDonacion(donacion);
        historial.setCiUsuario(ciUsuario);
        historial.setEstado(estado);
        historial.setImagenEvidencia(imagen);
        historial.setFechaActualizacion(new java.sql.Timestamp(System.currentTimeMillis()));
        historial.setUbicacion(ubicacionGuardada);

        historialSeguimientoDonacionesRepository.save(historial);
    }



}