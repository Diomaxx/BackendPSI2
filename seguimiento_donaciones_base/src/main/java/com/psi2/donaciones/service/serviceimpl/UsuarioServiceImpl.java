package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.dto.UsuarioDto;
import com.psi2.donaciones.entities.entitySQL.Usuario;
import com.psi2.donaciones.mapper.UsuarioMapper;
import com.psi2.donaciones.repository.UsuarioRepository;
import com.psi2.donaciones.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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

}
