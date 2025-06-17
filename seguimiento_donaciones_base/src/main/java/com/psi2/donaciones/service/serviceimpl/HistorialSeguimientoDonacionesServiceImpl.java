package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.HistorialSeguimientoDonacionesDto;
import com.psi2.donaciones.dto.ReporteCompletoHistorialDto;
import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.entities.entitySQL.HistorialSeguimientoDonaciones;
import com.psi2.donaciones.entities.entitySQL.Ubicacion;
import com.psi2.donaciones.entities.entityMongo.SeguimientoDonacion;
import com.psi2.donaciones.mapper.HistorialSeguimientoDonacionesMapper;
import com.psi2.donaciones.repository.HistorialSeguimientoDonacionesRepository;
import com.psi2.donaciones.repository.UbicacionRepository;
import com.psi2.donaciones.repository.DonacionRepository;
import com.psi2.donaciones.repository.SeguimientoDonacionRepository;
import com.psi2.donaciones.service.HistorialSeguimientoDonacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class HistorialSeguimientoDonacionesServiceImpl implements HistorialSeguimientoDonacionesService {

    @Autowired
    private HistorialSeguimientoDonacionesRepository historialSeguimientoDonacionesRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;
    
    @Autowired
    private DonacionRepository donacionRepository;
    
    @Autowired
    private SeguimientoDonacionRepository seguimientoDonacionRepository;

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

    @Override
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

    @Override
    public ReporteCompletoHistorialDto generarReporteCompletoHistorial(Integer donacionId) {
        Donacion donacion = donacionRepository.findById(donacionId)
            .orElseThrow(() -> new RuntimeException("Donación no encontrada con ID: " + donacionId));
        
        ReporteCompletoHistorialDto reporte = new ReporteCompletoHistorialDto();
        
        reporte.setIdDonacion(donacion.getIdDonacion());
        reporte.setCodigoDonacion(donacion.getCodigo());
        reporte.setFechaAprobacion(donacion.getFechaAprobacion());
        reporte.setFechaEntrega(donacion.getFechaEntrega());
        reporte.setCategoriaDonacion(donacion.getCategoria());
        
        if (donacion.getEncargado() != null) {
            reporte.setCiEncargado(donacion.getEncargado().getCi());
            reporte.setNombreEncargado(donacion.getEncargado().getNombre());
            reporte.setEmailEncargado(donacion.getEncargado().getCorreoElectronico());
            reporte.setTelefonoEncargado(donacion.getEncargado().getTelefono());
        }
        
        if (donacion.getSolicitud() != null) {
            reporte.setIdSolicitud(donacion.getSolicitud().getIdSolicitud());
            reporte.setFechaInicioIncendio(donacion.getSolicitud().getFechaInicioIncendio());
            reporte.setFechaSolicitud(donacion.getSolicitud().getFechaSolicitud());
            reporte.setSolicitudAprobada(donacion.getSolicitud().getAprobada());
            reporte.setCantidadPersonas(donacion.getSolicitud().getCantidadPersonas());
            reporte.setJustificacion(donacion.getSolicitud().getJustificacion());
            reporte.setCategoriaSolicitud(donacion.getSolicitud().getCategoria());
            reporte.setListaProductos(donacion.getSolicitud().getListaProductos());
            
            if (donacion.getSolicitud().getSolicitante() != null) {
                reporte.setCiSolicitante(donacion.getSolicitud().getSolicitante().getCi());
                reporte.setNombreSolicitante(donacion.getSolicitud().getSolicitante().getNombre());
                reporte.setApellidoSolicitante(donacion.getSolicitud().getSolicitante().getApellido());
                reporte.setTelefonoSolicitante(donacion.getSolicitud().getSolicitante().getTelefono());
                reporte.setEmailSolicitante(donacion.getSolicitud().getSolicitante().getEmail());
            }
            
            if (donacion.getSolicitud().getDestino() != null) {
                reporte.setComunidadDestino(donacion.getSolicitud().getDestino().getComunidad());
                reporte.setDireccionDestino(donacion.getSolicitud().getDestino().getDireccion());
                reporte.setProvinciaDestino(donacion.getSolicitud().getDestino().getProvincia());
                reporte.setLatitudDestino(donacion.getSolicitud().getDestino().getLatitud());
                reporte.setLongitudDestino(donacion.getSolicitud().getDestino().getLongitud());
            }
        }
        
        List<SeguimientoDonacion> seguimientos = seguimientoDonacionRepository.findAll()
            .stream()
            .filter(s -> s.getIdDonacion() != null && s.getIdDonacion().equals(String.valueOf(donacion.getIdDonacion())))
            .collect(Collectors.toList());
            
        if (!seguimientos.isEmpty()) {
            SeguimientoDonacion seguimientoActual = seguimientos.get(0);
            reporte.setEstadoActual(seguimientoActual.getEstado());
            reporte.setLatitudActual(seguimientoActual.getLatitud());
            reporte.setLongitudActual(seguimientoActual.getLongitud());
            reporte.setTimestampActual(seguimientoActual.getTimestamp());
        }
        
        List<HistorialSeguimientoDonaciones> historial = historialSeguimientoDonacionesRepository
            .findByDonacion_IdDonacion(donacion.getIdDonacion())
            .stream()
            .sorted(Comparator.comparing(HistorialSeguimientoDonaciones::getFechaActualizacion))
            .collect(Collectors.toList());
        
        List<ReporteCompletoHistorialDto.PuntoHistorialDto> puntosHistorial = new ArrayList<>();
        
        for (HistorialSeguimientoDonaciones punto : historial) {
            ReporteCompletoHistorialDto.PuntoHistorialDto puntoDto = new ReporteCompletoHistorialDto.PuntoHistorialDto();
            puntoDto.setIdHistorial(punto.getIdHistorial());
            puntoDto.setCiUsuario(punto.getCiUsuario());
            puntoDto.setEstado(punto.getEstado());
            puntoDto.setFechaActualizacion(punto.getFechaActualizacion());
            
            if (punto.getUbicacion() != null) {
                puntoDto.setLatitud(punto.getUbicacion().getLatitud());
                puntoDto.setLongitud(punto.getUbicacion().getLongitud());
            }
            
            puntosHistorial.add(puntoDto);
        }
        
        reporte.setPuntosHistorial(puntosHistorial);
        
        reporte.setTotalPuntosHistorial(puntosHistorial.size());
        reporte.setDistanciaRecorrida(calcularDistanciaTotal(puntosHistorial));
        reporte.setTiempoTotalDias(calcularTiempoTotalDias(donacion));
        reporte.setFechaGeneracionReporte(LocalDateTime.now());
        
        return reporte;
    }
    

    private Double calcularDistanciaTotal(List<ReporteCompletoHistorialDto.PuntoHistorialDto> puntos) {
        if (puntos == null || puntos.size() < 2) {
            return 0.0;
        }
        
        double distanciaTotal = 0.0;
        
        for (int i = 1; i < puntos.size(); i++) {
            ReporteCompletoHistorialDto.PuntoHistorialDto puntoAnterior = puntos.get(i - 1);
            ReporteCompletoHistorialDto.PuntoHistorialDto puntoActual = puntos.get(i);
            
            if (puntoAnterior.getLatitud() != null && puntoAnterior.getLongitud() != null
                && puntoActual.getLatitud() != null && puntoActual.getLongitud() != null) {
                
                double distancia = calcularDistanciaHaversine(
                    puntoAnterior.getLatitud(), puntoAnterior.getLongitud(),
                    puntoActual.getLatitud(), puntoActual.getLongitud()
                );
                distanciaTotal += distancia;
            }
        }
        
        return Math.round(distanciaTotal * 100.0) / 100.0;
    }
    

    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        
        return distance;
    }

    private Long calcularTiempoTotalDias(Donacion donacion) {
        if (donacion.getFechaAprobacion() == null) return null;
        
        long fechaFin = donacion.getFechaEntrega() != null 
            ? donacion.getFechaEntrega().getTime()
            : System.currentTimeMillis();
            
        long diferencia = fechaFin - donacion.getFechaAprobacion().getTime();
        return TimeUnit.MILLISECONDS.toDays(diferencia);
    }

}