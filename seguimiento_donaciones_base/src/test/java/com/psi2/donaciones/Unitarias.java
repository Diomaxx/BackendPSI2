package com.psi2.donaciones;

import com.psi2.donaciones.dto.UsuarioDto;
import com.psi2.donaciones.entities.entitySQL.Donacion;
import com.psi2.donaciones.entities.entitySQL.Usuario;
import com.psi2.donaciones.repository.DonacionRepository;
import com.psi2.donaciones.repository.UsuarioRepository;
import com.psi2.donaciones.service.serviceimpl.DonacionServiceImpl;
import com.psi2.donaciones.service.serviceimpl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*; // Para assertEquals, etc.
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class Unitarias {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DonacionRepository donacionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @InjectMocks
    private DonacionServiceImpl donacionService;

    @Test
    void testCreateUsuario() {
        UsuarioDto dto = new UsuarioDto(null,
                "Camila", "Soruco",
                "71234567", "camila.soruco@ejemplo.com",
                "8934561",
                "ContrasenaSegura123!",
                false, true
        );

        String passEncriptada = "$2a$10$hashficticioDeEjemplo";

        when(passwordEncoder.encode("ContrasenaSegura123!")).thenReturn(passEncriptada);

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setIdUsuario(1);
        usuarioGuardado.setNombre("Camila");
        usuarioGuardado.setApellido("Soruco");
        usuarioGuardado.setTelefono("71234567");
        usuarioGuardado.setCorreoElectronico("camila.soruco@ejemplo.com");
        usuarioGuardado.setCi("8934561");
        usuarioGuardado.setActive(true);
        usuarioGuardado.setAdmin(false);
        usuarioGuardado.setContrasena(passEncriptada);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);


        UsuarioDto resultado = usuarioService.createUsuario(dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        assertEquals("Camila", resultado.getNombre());
        assertEquals("Soruco", resultado.getApellido());
        assertEquals("71234567", resultado.getTelefono());
        assertEquals("camila.soruco@ejemplo.com", resultado.getCorreoElectronico());
        assertEquals("8934561", resultado.getCi());
        assertFalse(resultado.getAdmin());
        assertTrue(resultado.getActive());
        assertNull(resultado.getContrasena());
    }

    @Test
    void testToggleActiveUsuario() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setIdUsuario(1);
        usuarioExistente.setActive(true);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        UsuarioDto resultado = usuarioService.toggleActive(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        assertFalse(resultado.getActive());
        assertNull(resultado.getContrasena());

        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }


    @Test
    void testCalcularTiempoPromedioEntrega_validoEInvalido() {
        Donacion donacionValida1 = new Donacion();
        donacionValida1.setFechaAprobacion(java.sql.Date.valueOf("2024-06-01"));
        donacionValida1.setFechaEntrega(java.sql.Date.valueOf("2024-06-03"));

        Donacion donacionValida2 = new Donacion();
        donacionValida2.setFechaAprobacion(java.sql.Date.valueOf("2024-06-05"));
        donacionValida2.setFechaEntrega(java.sql.Date.valueOf("2024-06-06"));

        Donacion donacionInvalida = new Donacion();
        donacionInvalida.setFechaAprobacion(null);
        donacionInvalida.setFechaEntrega(java.sql.Date.valueOf("2024-06-10"));

        List<Donacion> donaciones = List.of(donacionValida1, donacionValida2, donacionInvalida);

        when(donacionRepository.findAll()).thenReturn(donaciones);

        double promedioDias = donacionService.calcularTiempoPromedioEntrega();

        assertEquals(1.5, promedioDias, 0.001, "El promedio debería ser 1.5 días (entre 2 y 1)");
    }


}
