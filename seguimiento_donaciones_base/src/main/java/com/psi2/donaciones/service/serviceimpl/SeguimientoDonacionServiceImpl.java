package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.HistorialSeguimientoDonacionesDto;
import com.psi2.donaciones.dto.SeguimientoCompletoDto;
import com.psi2.donaciones.dto.SeguimientoDonacionDto;
import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.entities.entityMongo.SeguimientoDonacion;
import com.psi2.donaciones.entities.entitySQL.HistorialSeguimientoDonaciones;
import com.psi2.donaciones.entities.entitySQL.Solicitud;
import com.psi2.donaciones.exception.ResourceNotFoundException;
import com.psi2.donaciones.mapper.SeguimientoDonacionMapper;
import com.psi2.donaciones.mapper.SolicitudMapper;
import com.psi2.donaciones.repository.DonacionRepository;
import com.psi2.donaciones.repository.HistorialSeguimientoDonacionesRepository;
import com.psi2.donaciones.repository.SeguimientoDonacionRepository;
import com.psi2.donaciones.repository.SolicitudRepository;
import com.psi2.donaciones.service.SeguimientoDonacionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeguimientoDonacionServiceImpl implements SeguimientoDonacionService {

    @Autowired
    private SeguimientoDonacionRepository seguimientoRepository;

    @Autowired
    private DonacionRepository donacionRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private HistorialSeguimientoDonacionesRepository historialSeguimientoDonacionesRepository;

    @Override
    public List<SeguimientoDonacionDto> getAllSeguimientos() {
        List<SeguimientoDonacion> seguimientoDonaciones = seguimientoRepository.findAll();
        return seguimientoDonaciones.stream().map(SeguimientoDonacionMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public SeguimientoDonacionDto buscarPorIdDonacion(Integer idDonacion) {
        String idDonacionStr = String.valueOf(idDonacion);

        List<SeguimientoDonacion> seguimientos = seguimientoRepository.findAll();

        return seguimientos.stream()
                .filter(s -> s.getIdDonacion() != null && s.getIdDonacion().equals(idDonacionStr))
                .findFirst()
                .map(SeguimientoDonacionMapper::toDto)
                .orElse(null);
    }

    public List<SeguimientoCompletoDto> obtenerTodosSeguimientosConHistorial() {

        List<SeguimientoDonacion> seguimientos = seguimientoRepository.findAll();
        List<HistorialSeguimientoDonaciones> todosLosHistoriales = historialSeguimientoDonacionesRepository.findAll();
        List<SeguimientoCompletoDto> resultado = new ArrayList<>();

        for (SeguimientoDonacion seguimiento : seguimientos) {

            Optional<Donacion> donacionOp = donacionRepository.findById(Integer.valueOf(seguimiento.getIdDonacion()));

            Donacion donacion = donacionOp.get();
            System.out.println(donacion);

            List<HistorialSeguimientoDonaciones> historialFiltrado = todosLosHistoriales.stream()
                    .filter(h -> h.getDonacion().getIdDonacion().equals(Integer.valueOf(seguimiento.getIdDonacion())))
                    .sorted(Comparator.comparing(HistorialSeguimientoDonaciones::getFechaActualizacion)).collect(Collectors.toList());

            List<SeguimientoCompletoDto.PuntoHistorialDto> puntosHistorial = historialFiltrado.stream()
                    .map(h -> new SeguimientoCompletoDto.PuntoHistorialDto(h.getUbicacion().getLatitud(), h.getUbicacion().getLongitud()))
                    .collect(Collectors.toList());

            SeguimientoCompletoDto dto = new SeguimientoCompletoDto();
            dto.setIdDonacion(seguimiento.getIdDonacion());
            dto.setCiUsuario(donacion.getEncargado().getCi());
            dto.setDestino(donacion.getSolicitud().getDestino().getDireccion());
            dto.setOrigen(donacion.getSolicitud().getDestino().getComunidad());
            dto.setId(seguimiento.getId());
            dto.setCodigo(donacion.getCodigo());
            dto.setEstado(seguimiento.getEstado());
            dto.setImagenEvidencia(seguimiento.getImagenEvidencia());
            dto.setLatitud(seguimiento.getLatitud());
            dto.setLongitud(seguimiento.getLongitud());
            dto.setTimestamp(seguimiento.getTimestamp());
            dto.setLatitud_destino(donacion.getSolicitud().getDestino().getLatitud());
            dto.setLongitud_destino(donacion.getSolicitud().getDestino().getLongitud());
            dto.setHistorial(puntosHistorial);

            resultado.add(dto);
        }

        return resultado;
    }


    @Override
    public void deleteSeguimientoByDonacionId(Integer donacionId) {
        SeguimientoDonacionDto seguimientoDonacionDto = buscarPorIdDonacion(donacionId);

        SeguimientoDonacion seguimientoDonacion = SeguimientoDonacionMapper.toEntity(seguimientoDonacionDto);

        if (seguimientoDonacion == null) {
            throw new RuntimeException("No se encontró un seguimiento para la donación con ID: " + donacionId);
        }

        seguimientoRepository.delete(seguimientoDonacion);
    }


    public long contarDonacionesEntregadas() {
        return donacionRepository.countByFechaEntregaIsNotNull();
    }
}
