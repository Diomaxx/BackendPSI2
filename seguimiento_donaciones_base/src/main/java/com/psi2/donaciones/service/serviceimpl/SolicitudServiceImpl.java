package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.*;
import com.psi2.donaciones.entities.entityMongo.SeguimientoDonacion;
import com.psi2.donaciones.entities.entityMongo.SolicitudesSinResponder;
import com.psi2.donaciones.entities.entitySQL.Destino;
import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.entities.entitySQL.Solicitante;
import com.psi2.donaciones.entities.entitySQL.Solicitud;
import com.psi2.donaciones.mapper.SolicitudMapper;
import com.psi2.donaciones.repository.*;
import com.psi2.donaciones.service.InventarioExternoService;
import com.psi2.donaciones.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private SolicitudesSinResponderRepository solicitudesSinResponderRepository;

    @Autowired
    private SolicitanteRepository solicitanteRepository;

    @Autowired
    private DestinoRepository destinoRepository;

    @Autowired
    private InventarioExternoService inventarioExternoService;

    @Autowired
    private DonacionRepository donacionRepository;

    @Autowired
    private SeguimientoDonacionRepository seguimientoDonacionRepository;

    @Override
    public List<SolicitudDto> getAllSolicitudes() {
        List<Solicitud> solicitudes = solicitudRepository.findAll();
        return solicitudes.stream()
                .map(SolicitudMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public long totalSolicitudes() {
        return solicitudRepository.count();
    }

    @Override
    public double calcularTiempoPromedioAprobacion(){
        List<Solicitud> solicitudes = solicitudRepository.findAll();
        if (solicitudes.isEmpty()) {
            return 0.0;
        }
        double sumaDias = 0.0;
        for (Solicitud solicitud : solicitudes) {
            long diferenciaMillis = solicitud.getFechaInicioIncendio().getTime() - solicitud.getFechaSolicitud().getTime();
            double dias = (double) diferenciaMillis / (1000 * 60 * 60 * 24);
            sumaDias += dias;
        }
        return sumaDias / solicitudes.size();
    }
    public Map<String, Integer> obtenerTop5ProductosMasSolicitados() {
        List<Solicitud> solicitudes = solicitudRepository.findAll();
        Map<String, Integer> conteoProductos = new HashMap<>();

        solicitudes.forEach(solicitud -> {
            if (solicitud.getListaProductos() != null) {
                String[] productos = solicitud.getListaProductos().split(",");

                for (String producto : productos) {

                    String nombreProducto = producto.replaceAll(":\\d+", "").trim();
                    conteoProductos.merge(nombreProducto, 1, Integer::sum);
                }
            }
        });

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(conteoProductos.entrySet());
        sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        Map<String, Integer> top5 = new LinkedHashMap<>();
        int limit = Math.min(5, sortedEntries.size());
        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Integer> entry = sortedEntries.get(i);
            top5.put(entry.getKey(), entry.getValue());
        }

        return top5;
    }

    public Map<String, Integer> obtenerSolicitudesPorProvincia() {
        return solicitudRepository.findAll().stream()
                .filter(solicitud -> Boolean.TRUE.equals(solicitud.getAprobada())) // solo aprobadas
                .collect(Collectors.groupingBy(
                        solicitud -> solicitud.getDestino() != null ? solicitud.getDestino().getProvincia() : "SIN PROVINCIA",
                        Collectors.summingInt(s -> 1)
                ));
    }
    public Map<String, Integer> obtenerSolicitudesPorMes() {
        return solicitudRepository.findAll().stream()
                .filter(s -> s.getFechaSolicitud() != null)
                .collect(Collectors.groupingBy(
                        s -> {
                            LocalDate fecha = s.getFechaSolicitud().toLocalDate();
                            return String.format("%02d/%d", fecha.getMonthValue(), fecha.getYear());
                        },
                        TreeMap::new,
                        Collectors.summingInt(s -> 1)
                ));
    }

    @Override
    public List<SolicitudDto> getSolicitudesAprobadas() {
        List<Solicitud> solicitudes = solicitudRepository.findAll().stream()
                .filter(s -> Boolean.TRUE.equals(s.getAprobada()))
                .collect(Collectors.toList());

        return solicitudes.stream()
                .map(SolicitudMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudListaDto> obtenerSolicitudesCompletas() {
        List<SolicitudListaDto> resultado = new ArrayList<>();

        List<Solicitud> solicitudes = solicitudRepository.findAll();

        for (Solicitud s : solicitudes) {

            Solicitante solicitante = solicitanteRepository.findById(s.getSolicitante().getIdSolicitante()).orElse(null);
            Destino destino = destinoRepository.findById(s.getDestino().getIdDestino()).orElse(null);

            SolicitanteDto solicitanteDto = solicitante != null ? new SolicitanteDto(
                    solicitante.getIdSolicitante(), solicitante.getNombre(),
                    solicitante.getApellido(), solicitante.getTelefono(),
                    solicitante.getCi()
            ) : null;

            DestinoDto destinoDto = destino != null ? new DestinoDto(destino.getIdDestino(), destino.getDireccion(),
                    destino.getProvincia(), destino.getComunidad(), destino.getLatitud(),
                    destino.getLongitud()
            ) : null;

            resultado.add(new SolicitudListaDto(String.valueOf(s.getIdSolicitud()), s.getFechaInicioIncendio(),
                    s.getFechaSolicitud(), s.getAprobada(),
                    s.getCantidadPersonas(), s.getJustificacion(),
                    s.getCategoria(), s.getListaProductos(),
                    solicitanteDto, destinoDto
            ));
        }

        List<SolicitudesSinResponder> solicitudesMongo = solicitudesSinResponderRepository.findAll();

        for (SolicitudesSinResponder s : solicitudesMongo) {

            Solicitante solicitante = solicitanteRepository.findById(Integer.parseInt(s.getIdSolicitante())).orElse(null);
            Destino destino = destinoRepository.findById(Integer.parseInt(s.getIdDestino())).orElse(null);

            SolicitanteDto solicitanteDto = solicitante != null ? new SolicitanteDto(
                    solicitante.getIdSolicitante(), solicitante.getNombre(), solicitante.getApellido(), solicitante.getTelefono(),
                    solicitante.getCi()
            ) : null;

            DestinoDto destinoDto = destino != null ? new DestinoDto(
                    destino.getIdDestino(), destino.getDireccion(), destino.getProvincia(), destino.getComunidad(),
                    destino.getLatitud(), destino.getLongitud()
            ) : null;

            String productosString = s.getListaProductos().stream().map(prod -> {
                String[] partes = prod.split(":");
                String idProducto = partes[0];
                String cantidad = partes[1];
                ProductoDto productoDto = inventarioExternoService.consultarProducto(idProducto);
                return productoDto.getNombre_articulo() + ":" + cantidad;
            }).collect(Collectors.joining(","));

            SolicitudListaDto SL = new SolicitudListaDto();
            SL.setIdSolicitud(s.getId());
            SL.setFechaSolicitud(new java.sql.Date(s.getFechaSolicitud().getTime()));
            SL.setFechaInicioIncendio(new java.sql.Date(s.getFechaInicioIncendio().getTime()));
            SL.setAprobada(null);
            SL.setCantidadPersonas(s.getCantidadPersonas());
            SL.setJustificacion(null);
            SL.setCategoria(s.getCategoria());
            SL.setProductos(productosString);
            SL.setDestino(destinoDto);
            SL.setSolicitante(solicitanteDto);
            resultado.add(SL);
        }

        return resultado;

    }

    @Override
    public List<SolicitudDonacionDto> obtenerSolicitudesConDonacionesPendientes() {
        List<SeguimientoDonacion> seguimientosPendientes = seguimientoDonacionRepository.findAll().stream()
                .filter(s -> {
                    String estado = s.getEstado();
                    return estado != null && estado.equalsIgnoreCase("Pendiente");
                })
                .collect(Collectors.toList());

        List<SolicitudDonacionDto> resultado = new ArrayList<>();

        for (SeguimientoDonacion seguimiento : seguimientosPendientes) {
            Optional<Donacion> donacion = donacionRepository.findById(Integer.valueOf(seguimiento.getIdDonacion()));

            if (donacion != null) {
                Solicitud solicitud = donacion.get().getSolicitud();
                if (solicitud != null) {
                    SolicitudDonacionDto dto = new SolicitudDonacionDto();
                    dto.setIdDonacion(donacion.get().getIdDonacion());
                    dto.setCodigo(donacion.get().getCodigo());
                    dto.setCiUsuario(donacion.get().getEncargado().getCi());
                    dto.setFecha_pedido(donacion.get().getFechaAprobacion());
                    dto.setDescripcion(solicitud.getListaProductos());
                    dto.setUbicacion(solicitud.getDestino().getDireccion());
                    dto.setLatitud_destino(solicitud.getDestino().getLatitud());
                    dto.setLongitud_destino(solicitud.getDestino().getLongitud());
                    resultado.add(dto);
                }
            }
        }

        return resultado;
    }

    @Override
    public List<SolicitudConPersonalDto> getSolicitudesConPersonal() {
        return solicitudRepository.findAll().stream()
                .filter(s -> Boolean.TRUE.equals(s.getAprobada()))
                .map(s -> new SolicitudConPersonalDto(
                        s.getIdSolicitud(),
                        s.getFechaInicioIncendio(),
                        s.getFechaSolicitud(),
                        s.getCantidadPersonas(),
                        s.getCategoria(),
                        s.getListaProductos(),
                        s.getSolicitante().getIdSolicitante(),
                        s.getDestino().getIdDestino(),
                        calcularPersonalNecesario(
                                s.getCategoria(),
                                s.getCantidadPersonas(),
                                s.getFechaInicioIncendio(),
                                s.getFechaSolicitud()
                        )
                ))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> calcularPersonalNecesario(String categoria, int cantidadPersonas, Date fechaInicio, Date fechaSolicitud) {
        Map<String, Integer> personal = new LinkedHashMap<>();

        long diasDiferencia = Math.max(1, (fechaSolicitud.getTime() - fechaInicio.getTime()) / (1000 * 60 * 60 * 24));
        double factorUrgencia;

        if (diasDiferencia <= 1) {
            factorUrgencia = 1.0;
        } else if (diasDiferencia <= 3) {
            factorUrgencia = 1.2;
        } else if (diasDiferencia <= 7) {
            factorUrgencia = 1.5;
        } else {
            factorUrgencia = 2.0;
        }

        switch (categoria.toLowerCase()) {
            case "incendio":
                personal.put("bombero", Math.max(1, (int) Math.ceil((cantidadPersonas / 8.0) * factorUrgencia)));
                personal.put("personal de salud", Math.max(2, (int) Math.ceil((cantidadPersonas / 7.0) * factorUrgencia)));
                personal.put("policía", Math.max(1, (int) Math.ceil((cantidadPersonas / 8.0) * factorUrgencia)));
                personal.put("veterinario", Math.max(1, (int) Math.ceil((cantidadPersonas / 10.0) * factorUrgencia)));
                if (cantidadPersonas > 30) {
                    personal.put("psicólogo", Math.max(1, (int) Math.ceil((cantidadPersonas / 30.0) * factorUrgencia)));
                    personal.put("voluntario", Math.max(1, (int) Math.ceil((cantidadPersonas / 9.0) * factorUrgencia)));
                }
                break;

            case "inundacion":
                personal.put("rescatista", Math.max(1, (int) Math.ceil((cantidadPersonas / 8.0) * factorUrgencia)));
                personal.put("personal de salud", Math.max(2, (int) Math.ceil((cantidadPersonas / 7.0) * factorUrgencia)));
                personal.put("voluntario", Math.max(2, (int) Math.ceil((cantidadPersonas / 6.0) * factorUrgencia)));
                personal.put("veterinario", Math.max(1, (int) Math.ceil((cantidadPersonas / 20.0) * factorUrgencia)));
                if (cantidadPersonas > 30) {
                    personal.put("cocinero comunitario", Math.max(1, (int) Math.ceil((cantidadPersonas / 15.0) * factorUrgencia)));
                    personal.put("logística", 1);
                }
                break;

            case "escasez":
                personal.put("voluntario", Math.max(2, (int) Math.ceil((cantidadPersonas / 10.0) * factorUrgencia)));
                personal.put("nutricionista", Math.max(1, (int) Math.ceil((cantidadPersonas / 12.0) * factorUrgencia)));
                personal.put("promotor comunitario", Math.max(1, (int) Math.ceil((cantidadPersonas / 12.0) * factorUrgencia)));
                if (cantidadPersonas > 40) {
                    personal.put("coordinador de alimentos", 1);
                    personal.put("psicólogo", 1);
                }
                break;

            case "epidemia":
                personal.put("médico", Math.max(1, (int) Math.ceil((cantidadPersonas / 12.0) * factorUrgencia)));
                personal.put("personal de salud", Math.max(2, (int) Math.ceil((cantidadPersonas / 8.0) * factorUrgencia)));
                personal.put("promotor de salud", Math.max(1, (int) Math.ceil((cantidadPersonas / 10.0) * factorUrgencia)));
                if (cantidadPersonas > 50) {
                    personal.put("psicólogo", 1);
                    personal.put("personal de bioseguridad", 1);
                    personal.put("veterinario", 1);
                }
                break;

            default:
                personal.put("voluntario comunitario", Math.max(2, (int) Math.ceil((cantidadPersonas / 7.0) * factorUrgencia)));
                if (cantidadPersonas > 40) {
                    personal.put("asistencia social", 1);
                }
                break;
        }

        return personal;
    }













}
