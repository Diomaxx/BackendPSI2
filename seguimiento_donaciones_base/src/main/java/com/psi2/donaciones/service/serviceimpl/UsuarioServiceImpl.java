package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.UsuarioDto;
import com.psi2.donaciones.entities.entitySQL.Usuario;
import com.psi2.donaciones.mapper.UsuarioMapper;
import com.psi2.donaciones.repository.UsuarioRepository;
import com.psi2.donaciones.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String ci) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCi(ci)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con CI: " + ci));

        return new User(
                usuario.getCi(),
                usuario.getContrasena(),
                Collections.emptyList()
        );
    }

    @Override
    public UsuarioDto createUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario();

        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setCorreoElectronico(usuarioDto.getCorreoElectronico());
        usuario.setCi(usuarioDto.getCi());

        enviarRegistroAGlobal(
                usuarioDto.getNombre(),
                usuarioDto.getApellido(),
                usuarioDto.getCorreoElectronico(),
                usuarioDto.getCi(),
                usuarioDto.getContrasena(),
                "777"
        );
        usuario.setContrasena(passwordEncoder.encode(usuarioDto.getContrasena()));

        Usuario saved = usuarioRepository.save(usuario);

        return new UsuarioDto(
                saved.getIdUsuario(),
                saved.getNombre(),
                saved.getApellido(),
                saved.getCorreoElectronico(),
                saved.getCi(),
                null
        );
    }

    @Override
    public List<UsuarioDto> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(UsuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDto getUsuarioByCi(String ci) {
        List<Usuario> usuarios = usuarioRepository.findAll();

        UsuarioMapper usuarioMapper = new UsuarioMapper();
        for (Usuario usuario : usuarios) {
            if (usuario.getCi().equals(ci)) {
                return usuarioMapper.toDto(usuario);
            }
        }

        throw new RuntimeException("Usuario no encontrado con CI: " + ci);
    }

    @Override
    public UsuarioDto registerFromGlobal(String nombre, String apellido, String email, String ci, String password, String telefono) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreoElectronico(email);
        usuario.setCi(ci);

        usuario.setContrasena(passwordEncoder.encode(password));

        Usuario saved = usuarioRepository.save(usuario);

        return new UsuarioDto(
                saved.getIdUsuario(),
                saved.getNombre(),
                saved.getApellido(),
                saved.getCorreoElectronico(),
                saved.getCi(),
                null
        );
    }

    private void enviarRegistroAGlobal(String nombre, String apellido, String email, String ci, String password, String telefono) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://34.9.138.238:2020/global_registro/alasE";

        Map<String, String> body = new HashMap<>();
        body.put("nombre", nombre);
        body.put("apellido", apellido);
        body.put("email", email);
        body.put("ci", ci);
        body.put("password", password);
        body.put("telefono", telefono);

        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("Registro enviado exitosamente a global.");
        } catch (Exception e) {
            System.err.println("Error al enviar registro global: " + e.getMessage());
        }
    }


}
