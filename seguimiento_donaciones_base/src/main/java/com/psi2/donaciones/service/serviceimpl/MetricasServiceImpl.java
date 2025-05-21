package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.entities.entityMongo.Metricas;
import com.psi2.donaciones.dto.MetricasDto;
import com.psi2.donaciones.dto.SolicitudDto;
import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.entities.entitySQL.Solicitud;
import com.psi2.donaciones.mapper.MetricasMapper;
import com.psi2.donaciones.mapper.SolicitudMapper;
import com.psi2.donaciones.repository.DonacionRepository;
import com.psi2.donaciones.repository.MetricasRepository;
import com.psi2.donaciones.repository.SolicitudRepository;
import com.psi2.donaciones.repository.SolicitudesSinResponderRepository;
import com.psi2.donaciones.service.DonacionService;
import com.psi2.donaciones.service.MetricasService;
import com.psi2.donaciones.service.SeguimientoDonacionService;
import com.psi2.donaciones.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class MetricasServiceImpl implements MetricasService {
    @Autowired
    private MetricasRepository metricasRepository;
    @Autowired
    private DonacionService donacionService;
    @Autowired
    private SeguimientoDonacionService seguimientoDonacionService;
    @Autowired
    private SolicitudService solicitudService;
    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private DonacionRepository donacionRepository;
    @Autowired
    private SolicitudesSinResponderRepository solicitudesSinResponderRepository;



    @Override
    public MetricasDto addResumenGeneral(MetricasDto metricasDto) {
        long totalDonaciones = donacionService.contarTotalDonaciones();
        metricasDto.setSolicitudesAprobadas((int) totalDonaciones);
        Metricas metrica = MetricasMapper.toEntity(metricasDto);
        Metricas nuevaMetrica = metricasRepository.save(metrica);
        return MetricasMapper.toDto(nuevaMetrica);
    }

    @Override
    public MetricasDto obtenerMetricas() {

        Metricas nueva = new Metricas();
        nueva.setFechaCreacion(LocalDateTime.now());
        nueva.setTotalSolicitudesRecibidas((int)solicitudService.totalSolicitudes());
        nueva.setSolicitudesAprobadas((int)donacionService.contarTotalDonaciones());
        nueva.setSolicitudesRechazadas((int)solicitudRepository.countByAprobadaFalse());
        nueva.setDonacionesEntregadas((int)seguimientoDonacionService.contarDonacionesEntregadas());
        nueva.setDonacionesPendientes((int)(donacionService.contarTotalDonaciones()-seguimientoDonacionService.contarDonacionesEntregadas()));
        nueva.setTiempoPromedioRespuesta(String.valueOf(calcularTiempoPromedioAprobacion()));

        nueva.setDonEntregadasProvincia(obtenerDonacionesEntregadasPorProvincia());
        nueva.setTiempoPromedioEntrega(String.valueOf(donacionService.calcularTiempoPromedioEntrega()));

        nueva.setSolicitudesSinResponder((int)solicitudesSinResponderRepository.countSolicitudesSinResponder());
        nueva.setTopProductosMasSolicitados(solicitudService.obtenerTop5ProductosMasSolicitados());
        nueva.setSolicitudesPorProvincia(solicitudService.obtenerSolicitudesPorProvincia());
        nueva.setSolicitudesPorMes(solicitudService.obtenerSolicitudesPorMes());
        System.out.println(nueva);

        return MetricasMapper.toDto(metricasRepository.save(nueva));
    }

    @Override
    public MetricasDto mostrarMetricas() {
        return metricasRepository.findTopByOrderByFechaCreacionDesc()
                .map(MetricasMapper::toDto)
                .orElseGet(() -> {
                    MetricasDto metricasDto = new MetricasDto();
                    metricasDto.setId("No hay métricas disponibles");
                    return metricasDto;
                });
    }

    @Override
    public double calcularTiempoPromedioAprobacion() {

        try {
            List<Solicitud> solicitudes = solicitudRepository.findAll();
            double totalDias = 0;
            int count = 0;

            for (Solicitud solicitud : solicitudes) {
                List<Donacion> donaciones = donacionRepository.findBySolicitud_IdSolicitud(solicitud.getIdSolicitud());

                for (Donacion donacion : donaciones) {
                    if (donacion.getFechaAprobacion() != null && solicitud.getFechaSolicitud() != null) {
                        long diffMillis = donacion.getFechaAprobacion().getTime() - solicitud.getFechaSolicitud().getTime();
                        double dias = (double) diffMillis / (1000 * 60 * 60 * 24);
                        totalDias += dias;
                        count++;
                    }
                }
            }
            return count > 0 ? totalDias / count : 0.0;
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return -0.0;
    }
    public Map<String, Integer> obtenerDonacionesEntregadasPorProvincia()
    {
        Map<String, Integer> conteo = new HashMap<>();

        List<Donacion> donacionesEntregadas = donacionRepository.findByFechaEntregaIsNotNull();
        Set<String> idsSolicitudes = donacionesEntregadas.stream()
                .map(donacion -> donacion.getSolicitud().getIdSolicitud().toString())
                .collect(Collectors.toSet());

        Map<String, String> provinciaPorSolicitud = solicitudRepository.findByIdSolicitudIn(idsSolicitudes)
                .stream()
                .collect(Collectors.toMap(
                        solicitud -> solicitud.getIdSolicitud().toString(),
                        solicitud -> solicitud.getDestino() != null ? solicitud.getDestino().getProvincia() : "SIN PROVINCIA"
                ));

        donacionesEntregadas.forEach(donacion -> {
            String provincia = provinciaPorSolicitud.get(donacion.getSolicitud().getIdSolicitud());
            if (provincia != null) {
                conteo.merge(provincia, 1, Integer::sum);
            }
        });

        return conteo;
    }

}