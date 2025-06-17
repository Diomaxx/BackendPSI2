package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.*;
import com.psi2.donaciones.entities.entityMongo.Notificaciones;
import com.psi2.donaciones.entities.entityMongo.SeguimientoDonacion;
import com.psi2.donaciones.entities.entitySQL.Destino;
import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.entities.entitySQL.Solicitud;
import com.psi2.donaciones.entities.entitySQL.Usuario;
import com.psi2.donaciones.mapper.DonacionMapper;
import com.psi2.donaciones.mapper.NotificacionesMapper;
import com.psi2.donaciones.repository.*;
import com.psi2.donaciones.service.DonacionService;
import com.psi2.donaciones.service.HistorialSeguimientoDonacionesService;
import com.psi2.donaciones.service.SeguimientoDonacionService;
import com.psi2.donaciones.config.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DonacionServiceImpl implements DonacionService {

    @Autowired
    private DonacionRepository donacionRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private SeguimientoDonacionRepository seguimientoDonacionRepository;

    @Autowired
    private HistorialSeguimientoDonacionesService historialSeguimientoDonacionesService;

    @Autowired
    private SeguimientoDonacionService seguimientoDonacionService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacionesRepository notificacionesRepository;

    @Autowired
    private DestinoRepository destinoRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private InventarioExternoServiceImpl inventarioExternoServiceImpl;

    @Autowired
    private EmailService emailService;

    @Override
    public List<DonacionDto> getAllDonaciones() {
        List<Donacion> donaciones = donacionRepository.findAll();
        return donaciones.stream()
                .map(DonacionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NewDonacionDto> getAllNewDonaciones() {
        List<Donacion> donaciones = donacionRepository.findAll();

        return donaciones.stream().map(d -> {
            NewDonacionDto dto = new NewDonacionDto();
            dto.setIdDonacion(d.getIdDonacion());
            dto.setCodigo(d.getCodigo());
            dto.setFechaAprobacion(d.getFechaAprobacion());
            dto.setFechaEntrega(d.getFechaEntrega());
            dto.setCategoria(d.getCategoria());
            dto.setImagen(d.getImagen());
            dto.setEncargado(d.getEncargado());
            dto.setSolicitud(d.getSolicitud());

            SeguimientoDonacionDto seguimiento = seguimientoDonacionService.buscarPorIdDonacion(d.getIdDonacion());
            if (seguimiento != null) {
                dto.setEstado(seguimiento.getEstado());
            } else {
                dto.setEstado("Entregada");
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public DonacionDto progresarEstadoArmadoPaquete(Integer idDonacion, String ciEncargado, String imagen) {
        Optional<Donacion> donacionOpt = donacionRepository.findById(idDonacion);
        if (donacionOpt.isEmpty()) {
            throw new RuntimeException("Donación no encontrada con ID: " + idDonacion);
        }

        Donacion donacion = donacionOpt.get();
        String imagenPath = null;

        if (imagen != null && !imagen.isEmpty()) {
            try {
                imagenPath = procesarImagen(imagen);
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar la imagen.", e);
            }
        }

        List<SeguimientoDonacion> seguimientos = seguimientoDonacionRepository.findAll();

        SeguimientoDonacion seguimiento = seguimientos.stream()
                .filter(s -> s.getIdDonacion() != null && s.getIdDonacion().equals(idDonacion.toString()))
                .findFirst()
                .orElse(null);

        if (seguimiento == null) {
            seguimiento = new SeguimientoDonacion();
            seguimiento.setIdDonacion(idDonacion.toString());
        }

        seguimiento.setCiUsuario(ciEncargado);
        seguimiento.setImagenEvidencia(imagenPath);
        seguimiento.setTimestamp(new Date());

        String nuevoEstado;

        if (seguimiento.getEstado() == null || !"Iniciando armado de paquete".equalsIgnoreCase(seguimiento.getEstado())) {
            nuevoEstado = "Iniciando armado de paquete";
        } else {
            nuevoEstado = "Paquete listo";
        }

        seguimiento.setEstado(nuevoEstado);
        seguimientoDonacionRepository.save(seguimiento);


        return DonacionMapper.toDto(donacion);
    }

    @Override
    public DonacionDto actualizarEntregaDonacion(Integer idDonacion, String nuevaCi, String estado, String imagen, Double latitud, Double longitud) {

        Optional<Donacion> donacionOpt = donacionRepository.findById(idDonacion);
        if (donacionOpt.isEmpty()) {
            throw new RuntimeException("Donación no encontrada con ID: " + idDonacion);
        }


        Donacion donacion = donacionOpt.get();
        String imagenPath = null;

        if (!donacion.getEncargado().getCi().equals(nuevaCi)) {
            Usuario nuevoEncargado = usuarioRepository.findByCi(nuevaCi)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con CI: " + nuevaCi));
            donacion.setEncargado(nuevoEncargado);
        }

        if (imagen != null && !imagen.isEmpty()) {
            try {
                imagenPath = procesarImagen(imagen);
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar la imagen.", e);
            }
        }

        List<SeguimientoDonacion> seguimientos = seguimientoDonacionRepository.findAll();

        SeguimientoDonacion seguimiento = seguimientos.stream()
                .filter(s -> s.getIdDonacion() != null && s.getIdDonacion().equals(idDonacion.toString()))
                .findFirst()
                .orElse(null);

        if (seguimiento == null) {
            // No existe -> crear nuevo
            seguimiento = new SeguimientoDonacion();
            seguimiento.setIdDonacion(idDonacion.toString());
            System.out.println("No encuentra");
        }

        seguimiento.setCiUsuario(nuevaCi);
        seguimiento.setImagenEvidencia(imagenPath);
        seguimiento.setLatitud(latitud);
        seguimiento.setLongitud(longitud);
        seguimiento.setTimestamp(new Date());

        // === CASO 1: ENTREGADO ===
        if ("Entregado".equalsIgnoreCase(estado)) {


            Solicitud solicitud = donacion.getSolicitud();
            if (solicitud == null || solicitud.getDestino() == null) {
                throw new RuntimeException("La donación no tiene una solicitud o destino asignado.");
            }

            double latDestino = donacion.getSolicitud().getDestino().getLatitud();
            double lonDestino = donacion.getSolicitud().getDestino().getLongitud();

            double distancia = calcularDistancia(latitud, longitud, latDestino, lonDestino);
            if (distancia > 1000) {
                System.out.println("Mucha idstancia" + distancia);

                throw new IllegalArgumentException("Debes estar a menos de 100 metros del destino para marcar como entregado.");
            }

            seguimiento.setEstado("Entregado");
            seguimientoDonacionRepository.save(seguimiento);

            donacion.setFechaEntrega(new java.sql.Date(new Date().getTime()));
            donacion.setImagen(imagenPath);
            seguimientoDonacionService.deleteSeguimientoByDonacionId(idDonacion);
            donacionRepository.save(donacion);

            historialSeguimientoDonacionesService.registrarHistorialSeguimiento(donacion, nuevaCi, "Entregado", imagenPath, latitud, longitud);

            return  DonacionMapper.toDto(donacion);
        }

        // === CASO 2: EN CAMINO ===
        if ("En camino".equalsIgnoreCase(estado)) {

            String nuevoEstado;

            if (!seguimiento.getEstado().startsWith("En camino")) {
                nuevoEstado = "En camino - Etapa 1";

                try {
                    enviarCorreoEnCamino(donacion);
                } catch (Exception e) {
                    System.err.println("Error al enviar correo de donación en camino: " + e.getMessage());
                }


            } else {
                switch (seguimiento.getEstado()) {
                    case "En camino - Etapa 1":
                        nuevoEstado = "En camino - Etapa 2";
                        break;
                    case "En camino - Etapa 2":
                        nuevoEstado = "En camino - Etapa 3";
                        break;
                    case "En camino - Etapa 3":
                        nuevoEstado = "En camino - Etapa 4";
                        break;
                    case "En camino - Etapa 4":
                        nuevoEstado = "En camino - Etapa 5";
                        break;
                    case "En camino - Etapa 5":
                        nuevoEstado = "Entregado";

                        seguimiento.setEstado(nuevoEstado);
                        seguimientoDonacionRepository.save(seguimiento);

                        donacion.setFechaEntrega(new java.sql.Date(new Date().getTime()));
                        donacion.setImagen(imagenPath);
                        seguimientoDonacionService.deleteSeguimientoByDonacionId(idDonacion);
                        donacionRepository.save(donacion);

                        historialSeguimientoDonacionesService.registrarHistorialSeguimiento(donacion, nuevaCi, "Entregado", imagenPath, latitud, longitud);

                        return  DonacionMapper.toDto(donacion);

                    default:
                        nuevoEstado = "En camino - Etapa 1";
                        break;
                }
            }

            seguimiento.setEstado(nuevoEstado);
            seguimientoDonacionRepository.save(seguimiento);
            historialSeguimientoDonacionesService.registrarHistorialSeguimiento(donacion, nuevaCi, nuevoEstado, imagenPath, latitud, longitud);
            donacionRepository.save(donacion);
            return  DonacionMapper.toDto(donacion);
        }

        // === CASO 3: OTRO ESTADO ===
        seguimiento.setEstado(estado);
        seguimientoDonacionRepository.save(seguimiento);
        historialSeguimientoDonacionesService.registrarHistorialSeguimiento(donacion, nuevaCi, estado, imagenPath, latitud, longitud);
        donacionRepository.save(donacion);

        return  DonacionMapper.toDto(donacion);
    }

    @Override
    public void crearDonacionDesdeSolicitud(Integer idSolicitud, String ciEncargado) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + idSolicitud));

        List<Usuario> usuarios = usuarioRepository.findAll();

        Usuario encargado = usuarios.stream()
                .filter(u -> u.getCi() != null && u.getCi().trim().equals(ciEncargado.trim()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Encargado no encontrado con CI: " + ciEncargado));

        Destino destino = solicitud.getDestino();
        if (destino == null) {
            throw new RuntimeException("La solicitud no tiene un destino asignado");
        }

        String comunidad = destino.getComunidad();
        if (comunidad == null || comunidad.isEmpty()) {
            comunidad = "DA";
        }

        String codigoDonacion = generarCodigoDonacion(comunidad);

        Donacion donacion = new Donacion();

        donacion.setCodigo(codigoDonacion);

        donacion.setFechaAprobacion(new java.sql.Date(System.currentTimeMillis()));

        donacion.setFechaEntrega(null);

        donacion.setImagen(null);

        donacion.setCategoria(solicitud.getCategoria());
        donacion.setEncargado(encargado);
        donacion.setSolicitud(solicitud);

        donacionRepository.save(donacion);

        NotificacionesDto dto = new NotificacionesDto();
        dto.setTitulo("Solicitud Aprobada");
        dto.setDescripcion("Se necesita crear armar el paquete para la donacion \"" + codigoDonacion + "\" a almacén para la preparación del paquete");
        dto.setTipo("Solicitud");
        dto.setNivelSeveridad("Media");
        dto.setFechaCreacion(new java.util.Date());

        messagingTemplate.convertAndSend("/topic/nueva-aprobada", dto);
        messagingTemplate.convertAndSend("/topic/nueva-notificacion", dto);

        Notificaciones notificacion = NotificacionesMapper.toEntity(dto);
        notificacionesRepository.save(notificacion);

        SeguimientoDonacion seguimiento = new SeguimientoDonacion();
        seguimiento.setIdDonacion(donacion.getIdDonacion().toString());
        seguimiento.setCiUsuario(encargado.getCi());
        seguimiento.setEstado("Pendiente");
        seguimiento.setImagenEvidencia(null);
        seguimiento.setLatitud(-17.783315);
        seguimiento.setLongitud(-63.182126);
        seguimiento.setTimestamp(new Date());

        seguimientoDonacionRepository.save(seguimiento);

        historialSeguimientoDonacionesService.registrarHistorialSeguimiento(donacion, encargado.getCi(),
                "Pendiente", null,
                -17.783315, -63.182126);
    }


    @Override
    public long contarTotalDonaciones() {
        return donacionRepository.count();
    }

    private String procesarImagen(String imagenBase64) throws IOException {
        String base64Image = imagenBase64.contains(",")
                ? imagenBase64.split(",")[1]
                : imagenBase64;

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        String rutaSistema = "/var/www/html/imagenesDonacion/";
        String fileName = "imagen_" + System.currentTimeMillis() + ".jpg";
        String filePath = rutaSistema + fileName;

        File dir = new File(rutaSistema);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(imageBytes);
        }

        return "https://dasalas.shop/imagenesDonacion/" + fileName;
    }


    private String generarCodigoDonacion(String comunidad) {
        String[] palabras = comunidad.trim().split("\\s+");
        StringBuilder prefijo = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                prefijo.append(Character.toUpperCase(palabra.charAt(0)));
            }
        }
        String prefijoNombre = prefijo.toString();

        List<Donacion> todasLasDonaciones = donacionRepository.findAll();

        int maxNumero = 0;
        for (Donacion d : todasLasDonaciones) {
            String nombre = d.getCodigo();
            if (nombre != null && nombre.startsWith(prefijoNombre)) {
                String numeroStr = nombre.substring(prefijoNombre.length());
                try {
                    int numero = Integer.parseInt(numeroStr);
                    if (numero > maxNumero) {
                        maxNumero = numero;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        int nuevoNumero = maxNumero + 1;

        String numeroFormateado = (nuevoNumero < 10000)
                ? new DecimalFormat("0000").format(nuevoNumero)
                : Integer.toString(nuevoNumero);
        return prefijoNombre + numeroFormateado;
    }

    //
    @Override
    public double calcularTiempoPromedioEntrega() {
        List<Donacion> donaciones = donacionRepository.findAll();

        double totalDias = 0;
        int count = 0;

        for (Donacion donacion : donaciones) {
            if (donacion.getFechaAprobacion() != null && donacion.getFechaEntrega() != null) {
                long diffMillis = donacion.getFechaEntrega().getTime() - donacion.getFechaAprobacion().getTime();
                double dias = (double) diffMillis / (1000 * 60 * 60 * 24);
                totalDias += dias;
                count++;
            }
        }


        return count > 0 ? totalDias / count : 0.0;
    }

    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Radio de la Tierra en metros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // en metros
    }

    @Override
    public List<AgradecimientoDto> obtenerDonacionesConDonantes() {
        List<Donacion> donaciones = donacionRepository.findAll();

        return donaciones.stream().map(donacion -> {
            DonacionDto dto = DonacionMapper.toDto(donacion);

            List<DonanteDto> donantes = new ArrayList<>();
            try {
                donantes = inventarioExternoServiceImpl.obtenerDonantesPorCodigo(donacion.getCodigo());
            } catch (Exception e) {
                System.err.println("Error obteniendo donantes para donación " + donacion.getCodigo() + ": " + e.getMessage());
            }

            AgradecimientoDto combinado = new AgradecimientoDto();
            combinado.setIdDonacion(dto.getIdDonacion());
            combinado.setCodigo(dto.getCodigo());
            combinado.setImagen(dto.getImagen());
            combinado.setFechaEntrega(dto.getFechaEntrega());
            combinado.setDonantes(donantes);
            return combinado;
        }).collect(Collectors.toList());
    }
    

    private void enviarCorreoEnCamino(Donacion donacion) {
        if (donacion.getSolicitud() == null || donacion.getSolicitud().getSolicitante() == null) {
            System.err.println("La donación no tiene una solicitud o solicitante asociado");
            return;
        }
        
        var solicitante = donacion.getSolicitud().getSolicitante();
        var destino = donacion.getSolicitud().getDestino();
        
        if (solicitante.getEmail() == null || solicitante.getEmail().isEmpty()) {
            System.err.println("El solicitante no tiene un email configurado");
            return;
        }
        
        String asunto = "Su donación está en camino - Código: " + donacion.getCodigo();
        
        StringBuilder contenido = new StringBuilder();
        contenido.append("Estimado/a ").append(solicitante.getNombre()).append(" ").append(solicitante.getApellido()).append(",\n\n");
        contenido.append("¡Excelentes noticias! Su donación ya está en camino hacia su destino.\n\n");
        contenido.append("Detalles del envío:\n");
        contenido.append("- Código de donación: ").append(donacion.getCodigo()).append("\n");
        contenido.append("- Categoría: ").append(donacion.getCategoria()).append("\n");
        contenido.append("- Destino: ").append(destino != null ? destino.getDireccion() + ", " + destino.getProvincia() : "No especificado").append("\n");
        
        if (destino != null && destino.getComunidad() != null) {
            contenido.append("- Comunidad: ").append(destino.getComunidad()).append("\n");
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        contenido.append("- Fecha de salida: ").append(dateFormatter.format(new Date())).append("\n\n");
        
        contenido.append("Nuestro equipo se encuentra en ruta hacia el destino indicado. ");
        contenido.append("Atentamente,\n");
        contenido.append("Equipo de D.A.S.\n");

        emailService.enviarCorreo(solicitante.getEmail(), asunto, contenido.toString());
    }


    @Override
    public DonacionDto cambiarDestinoDonacion(Integer idDonacion, DestinoDto destinoDto) {
        Optional<Donacion> donacionOpt = donacionRepository.findById(idDonacion);
        if (donacionOpt.isEmpty()) {
            throw new RuntimeException("Donación no encontrada con ID: " + idDonacion);
        }

        Donacion donacion = donacionOpt.get();
        
        if (donacion.getSolicitud() == null) {
            throw new RuntimeException("La donación no tiene una solicitud asociada");
        }

        Solicitud solicitud = donacion.getSolicitud();
        
        Destino destinoActual = solicitud.getDestino();
        if (destinoActual == null) {
            throw new RuntimeException("La solicitud no tiene un destino asignado");
        }

        if (destinoDto.getProvincia() != null && !destinoDto.getProvincia().trim().isEmpty()) {
            if (!destinoDto.getProvincia().equals(destinoActual.getProvincia())) {
                destinoActual.setProvincia(destinoDto.getProvincia());
            }
        }
        
        if (destinoDto.getComunidad() != null && !destinoDto.getComunidad().trim().isEmpty()) {
            if (!destinoDto.getComunidad().equals(destinoActual.getComunidad())) {
                destinoActual.setComunidad(destinoDto.getComunidad());
            }
        }
        
        if (destinoDto.getDireccion() != null && !destinoDto.getDireccion().trim().isEmpty()) {
            if (!destinoDto.getDireccion().equals(destinoActual.getDireccion())) {
                destinoActual.setDireccion(destinoDto.getDireccion());
            }
        }
        
        if (destinoDto.getLatitud() != null) {
            if (!destinoDto.getLatitud().equals(destinoActual.getLatitud())) {
                destinoActual.setLatitud(destinoDto.getLatitud());
            }
        }
        
        if (destinoDto.getLongitud() != null) {
            if (!destinoDto.getLongitud().equals(destinoActual.getLongitud())) {
                destinoActual.setLongitud(destinoDto.getLongitud());
            }
        }
        

        destinoRepository.save(destinoActual);

        solicitudRepository.save(solicitud);
        

        NotificacionesDto notificacionDto = new NotificacionesDto();
        notificacionDto.setTitulo("Destino Modificado");
        notificacionDto.setDescripcion("Se ha modificado el destino de la donación \"" + donacion.getCodigo() + "\" a: " + destinoDto.getDireccion() + ", " + destinoDto.getProvincia());
        notificacionDto.setTipo("Modificación");
        notificacionDto.setNivelSeveridad("Media");
        notificacionDto.setFechaCreacion(new java.util.Date());

        messagingTemplate.convertAndSend("/topic/nueva-notificacion", notificacionDto);

        Notificaciones notificacion = NotificacionesMapper.toEntity(notificacionDto);
        notificacionesRepository.save(notificacion);

        return DonacionMapper.toDto(donacion);
    }


}
