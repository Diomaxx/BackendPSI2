package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.NotificacionesDto;
import com.psi2.donaciones.dto.ProductoDto;
import com.psi2.donaciones.entities.entityMongo.Notificaciones;
import com.psi2.donaciones.entities.entitySQL.Destino;
import com.psi2.donaciones.entities.entitySQL.Solicitante;
import com.psi2.donaciones.entities.entitySQL.Solicitud;
import com.psi2.donaciones.entities.entityMongo.SolicitudesSinResponder;
import com.psi2.donaciones.dto.SolicitudSinResponderDto;
import com.psi2.donaciones.mapper.NotificacionesMapper;
import com.psi2.donaciones.mapper.SolicitudSinResponderMapper;
import com.psi2.donaciones.repository.*;
import com.psi2.donaciones.request.CrearSSDRequest;
import com.psi2.donaciones.service.DonacionService;
import com.psi2.donaciones.service.InventarioExternoService;
import com.psi2.donaciones.service.SolicitudSinResponderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolicitudSinResponderServiceImpl implements SolicitudSinResponderService {

    @Autowired
    private SolicitudesSinResponderRepository solicitudesSinResponderRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;
    
    @Autowired
    private InventarioExternoService inventarioExternoService;
    
    @Autowired
    private DonacionService donacionService;

    @Autowired
    private DestinoRepository destinoRepository;

    @Autowired
    private SolicitanteRepository solicitanteRepository;

    @Autowired
    private NotificacionesRepository notificacionesRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Override
    public List<SolicitudSinResponderDto> obtenerTodasSolicitudes() {
        List<SolicitudesSinResponder> solicitudes = solicitudesSinResponderRepository.findAll();
        return solicitudes.stream()
                .map(SolicitudSinResponderMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public SolicitudSinResponderDto crearSolicitud(SolicitudSinResponderDto solicitudDto) {
        if (!verificarDisponibilidad(solicitudDto.getListaProductos())) {
            throw new RuntimeException("No hay suficiente stock disponible para algunos de los productos solicitados");
        }
        
        solicitudDto.setFechaSolicitud(new java.util.Date());
        
        SolicitudesSinResponder solicitud = SolicitudSinResponderMapper.toEntity(solicitudDto);
        SolicitudesSinResponder guardada = solicitudesSinResponderRepository.save(solicitud);
        
        return SolicitudSinResponderMapper.toDto(guardada);
    }

    @Override
    public SolicitudSinResponderDto crearSolicitudCompleta(CrearSSDRequest request) {

        List<Destino> destinos = destinoRepository.findAll();
        Destino destinoGuardado = null;

        for (Destino destino : destinos) {
            if (destino.getDireccion().equalsIgnoreCase(request.getDireccion()) &&
                    destino.getProvincia().equalsIgnoreCase(request.getProvincia())) {
                destinoGuardado = destino;
                break;
            }
        }

        if (destinoGuardado == null) {
            Destino nuevoDestino = new Destino();
            nuevoDestino.setProvincia(request.getProvincia());
            nuevoDestino.setComunidad(request.getComunidad());
            nuevoDestino.setDireccion(request.getDireccion());
            nuevoDestino.setLatitud(request.getLatitud());
            nuevoDestino.setLongitud(request.getLongitud());
            destinoGuardado = destinoRepository.save(nuevoDestino);
        }

        List<Solicitante> solicitantes = solicitanteRepository.findAll();
        Solicitante solicitanteGuardado = null;

        for (Solicitante solicitante : solicitantes) {
            if (solicitante.getCi().equals(request.getCiSolicitante())) {
                solicitanteGuardado = solicitante;
                break;
            }
        }

        if (solicitanteGuardado == null) {
            Solicitante nuevoSolicitante = new Solicitante();
            nuevoSolicitante.setNombre(request.getNombreSolicitante());
            nuevoSolicitante.setApellido(request.getApellidoSolicitante());
            nuevoSolicitante.setCi(request.getCiSolicitante());
            nuevoSolicitante.setTelefono(request.getTelefonoSolicitante());
            solicitanteGuardado = solicitanteRepository.save(nuevoSolicitante);
        }
        SolicitudesSinResponder solicitud = new SolicitudesSinResponder();
        solicitud.setFechaInicioIncendio(request.getFechaInicioIncendio());
        solicitud.setFechaSolicitud(new java.util.Date());
        solicitud.setListaProductos(request.getListaProductos());
        solicitud.setCantidadPersonas(request.getCantidadPersonas());
        solicitud.setCategoria(request.getCategoria());
        solicitud.setIdSolicitante(String.valueOf(solicitanteGuardado.getIdSolicitante()));
        solicitud.setIdDestino(String.valueOf(destinoGuardado.getIdDestino()));
        inventarioExternoService.verificarStockBajo();
        SolicitudesSinResponder guardada = solicitudesSinResponderRepository.save(solicitud);

        NotificacionesDto dto = new NotificacionesDto();
        dto.setTitulo("Nueva Solicitud ingresada");
        dto.setDescripcion("Nueva Solicitud desde \"" + destinoGuardado.getComunidad() + "\"");
        dto.setTipo("Solicitud");

        java.util.Date fechaActual = new java.util.Date();
        java.util.Date fechaInicio = new java.util.Date(solicitud.getFechaInicioIncendio().getTime());


        long diffMillis = fechaActual.getTime() - fechaInicio.getTime();
        long diffDias = diffMillis / (1000 * 60 * 60 * 24);

        String nivelSeveridad;
        if (diffDias >= 7) {
            nivelSeveridad = "Alta";
        } else if (diffDias >= 3) {
            nivelSeveridad = "Media";
        } else {
            nivelSeveridad = "Baja";
        }

        dto.setNivelSeveridad(nivelSeveridad);

        dto.setFechaCreacion(new java.util.Date());

        messagingTemplate.convertAndSend("/topic/nueva-notificacion", dto);

        Notificaciones notificacion = NotificacionesMapper.toEntity(dto);
        notificacionesRepository.save(notificacion);

        return SolicitudSinResponderMapper.toDto(guardada);
    }


    @Override
    public boolean aprobarSolicitud(String idSolicitud, String ciEncargado) {

        Optional<SolicitudesSinResponder> optSolicitud = solicitudesSinResponderRepository.findById(idSolicitud);
        if (optSolicitud.isEmpty()) {
            return false;
        }

        SolicitudesSinResponder solicitudSinResponder = optSolicitud.get();

        try {
            Integer idsolicitante= Integer.parseInt(solicitudSinResponder.getIdSolicitante());
            Optional<Solicitante> optSolicitante = solicitanteRepository.findById(idsolicitante);
            if (optSolicitante.isEmpty()) {
                throw new RuntimeException("Solicitante no encontrado con CI: " + solicitudSinResponder.getIdSolicitante());
            }
            Solicitante solicitante = optSolicitante.get();

            Integer idDestino = Integer.parseInt(solicitudSinResponder.getIdDestino());
            Optional<Destino> optDestino = destinoRepository.findById(idDestino);
            if (optDestino.isEmpty()) {
                throw new RuntimeException("Destino no encontrado con ID: " + solicitudSinResponder.getIdDestino());
            }
            Destino destino = optDestino.get();

            Solicitud solicitud = new Solicitud();
            solicitud.setFechaInicioIncendio(new java.sql.Date(solicitudSinResponder.getFechaInicioIncendio().getTime()));
            solicitud.setFechaSolicitud(new java.sql.Date(solicitudSinResponder.getFechaSolicitud().getTime()));
            solicitud.setAprobada(true);
            solicitud.setCantidadPersonas(solicitudSinResponder.getCantidadPersonas());
            solicitud.setCategoria(solicitudSinResponder.getCategoria());
            solicitud.setJustificacion("Aprobada sin observaciones");

            List<String> listaProductosFormateada = solicitudSinResponder.getListaProductos().stream()
                    .map(item -> {
                        String[] partes = item.split(":");
                        if (partes.length == 2) {
                            String idProducto = partes[0];
                            String cantidad = partes[1];

                            ProductoDto producto = inventarioExternoService.consultarProducto(idProducto);
                            String nombreProducto = producto != null ? producto.getNombre_articulo() : idProducto;

                            return nombreProducto + ":" + cantidad;
                        }
                        return item;
                    })
                    .toList();

            solicitud.setListaProductos(String.join(",", listaProductosFormateada));

            solicitud.setSolicitante(solicitante);
            solicitud.setDestino(destino);

            solicitudRepository.save(solicitud);

            inventarioExternoService.verificarStockBajo();
            
            donacionService.crearDonacionDesdeSolicitud(solicitud.getIdSolicitud(),ciEncargado);
            
            solicitudesSinResponderRepository.deleteById(idSolicitud);


            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al aprobar la solicitud: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean rechazarSolicitud(String idSolicitud, String justificacion) {
        Optional<SolicitudesSinResponder> optSolicitud = solicitudesSinResponderRepository.findById(idSolicitud);
        if (optSolicitud.isEmpty()) {
            return false;
        }

        SolicitudesSinResponder solicitudSinResponder = optSolicitud.get();

        try {
            Integer idsolicitante= Integer.parseInt(solicitudSinResponder.getIdSolicitante());
            Optional<Solicitante> optSolicitante = solicitanteRepository.findById(idsolicitante);
            if (optSolicitante.isEmpty()) {
                throw new RuntimeException("Solicitante no encontrado con ID: " + solicitudSinResponder.getIdSolicitante());
            }
            Solicitante solicitante = optSolicitante.get();

            Integer idDestino = Integer.parseInt(solicitudSinResponder.getIdDestino());
            Optional<Destino> optDestino = destinoRepository.findById(idDestino);
            if (optDestino.isEmpty()) {
                throw new RuntimeException("Destino no encontrado con ID: " + solicitudSinResponder.getIdDestino());
            }
            Destino destino = optDestino.get();

            Solicitud solicitud = new Solicitud();
            solicitud.setFechaInicioIncendio(new java.sql.Date(solicitudSinResponder.getFechaInicioIncendio().getTime()));
            solicitud.setFechaSolicitud(new java.sql.Date(solicitudSinResponder.getFechaSolicitud().getTime()));
            solicitud.setAprobada(false);
            solicitud.setCantidadPersonas(solicitudSinResponder.getCantidadPersonas());
            solicitud.setCategoria(solicitudSinResponder.getCategoria());
            solicitud.setJustificacion(justificacion);

            List<String> listaProductosFormateada = solicitudSinResponder.getListaProductos().stream()
                    .map(item -> {
                        String[] partes = item.split(":");
                        if (partes.length == 2) {
                            String idProducto = partes[0];
                            String cantidad = partes[1];

                            ProductoDto producto = inventarioExternoService.consultarProducto(idProducto);
                            String nombreProducto = producto != null ? producto.getNombre_articulo() : idProducto;

                            return nombreProducto + ":" + cantidad;
                        }
                        return item;
                    })
                    .toList();

            solicitud.setListaProductos(String.join(",", listaProductosFormateada));

            solicitud.setSolicitante(solicitante);
            solicitud.setDestino(destino);

            solicitudRepository.save(solicitud);

            solicitudesSinResponderRepository.deleteById(idSolicitud);

            return true;

        } catch (Exception e) {
            throw new RuntimeException("Error al rechazar la solicitud: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean verificarDisponibilidad(List<String> listaProductos) {
        Map<String, Integer> disponibilidadActual = calcularDisponibilidadActual();
        
        Map<String, Integer> productosRequeridos = new HashMap<>();
        
        for (String item : listaProductos) {
            String[] partes = item.split(":");
            if (partes.length == 2) {
                String idProducto = partes[0];
                try {
                    int cantidad = Integer.parseInt(partes[1]);
                    
                    int cantidadActual = productosRequeridos.getOrDefault(idProducto, 0);
                    productosRequeridos.put(idProducto, cantidadActual + cantidad);
                } catch (NumberFormatException e) {
                    // Ignora formato correcto
                }
            }
        }
        
        for (Map.Entry<String, Integer> entry : productosRequeridos.entrySet()) {
            String idProducto = entry.getKey();
            int cantidadRequerida = entry.getValue();
            
            if (!disponibilidadActual.containsKey(idProducto) ||
                disponibilidadActual.get(idProducto) < cantidadRequerida) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public Map<String, Integer> obtenerInventarioDisponible() {
        return calcularDisponibilidadActual();
    }

    
    private Map<String, Integer> calcularDisponibilidadActual() {
        Map<String, Integer> inventarioActual = new HashMap<>();
        inventarioExternoService.consultarInventario().forEach(producto -> 
            inventarioActual.put(producto.getId_articulo(), producto.getTotal_restante())
        );
        
        Map<String, Integer> reservas = calcularReservas();
        
        Map<String, Integer> disponibilidadReal = new HashMap<>(inventarioActual);
        
        for (Map.Entry<String, Integer> entry : reservas.entrySet()) {
            String idProducto = entry.getKey();
            Integer cantidadReservada = entry.getValue();
            
            if (disponibilidadReal.containsKey(idProducto)) {
                int cantidadDisponible = disponibilidadReal.get(idProducto);
                disponibilidadReal.put(idProducto, cantidadDisponible - cantidadReservada);
            }
        }
        
        return disponibilidadReal;
    }

    private Map<String, Integer> calcularReservas() {
        Map<String, Integer> reservas = new HashMap<>();

        List<SolicitudesSinResponder> solicitudes = solicitudesSinResponderRepository.findAll();

        for (SolicitudesSinResponder solicitud : solicitudes) {
            Map<String, Integer> productosSolicitud = obtenerProductosDeSolicitud(solicitud);

            for (Map.Entry<String, Integer> entry : productosSolicitud.entrySet()) {
                String idProducto = entry.getKey();
                int cantidad = entry.getValue();

                int reservaActual = reservas.getOrDefault(idProducto, 0);
                reservas.put(idProducto, reservaActual + cantidad);
            }
        }

        return reservas;
    }
    
    private Map<String, Integer> obtenerProductosDeSolicitud(SolicitudesSinResponder solicitud) {
        Map<String, Integer> productos = new HashMap<>();
        
        for (String item : solicitud.getListaProductos()) {
            String[] partes = item.split(":");
            if (partes.length == 2) {
                try {
                    String idProducto = partes[0];
                    int cantidad = Integer.parseInt(partes[1]);

                    int cantidadActual = productos.getOrDefault(idProducto, 0);
                    productos.put(idProducto, cantidadActual + cantidad);
                } catch (NumberFormatException e) {
                }
            }
        }
        
        return productos;
    }
} 