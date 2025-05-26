package com.psi2.donaciones.service;

import com.psi2.donaciones.dto.UsuarioDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsuarioService extends UserDetailsService {
    UsuarioDto createUsuario(UsuarioDto usuarioDto);
    List<UsuarioDto> getAllUsuarios();
    UsuarioDto getUsuarioByCi(String ci);
    List<UsuarioDto> getAllUsuariosNoAdmin();
    UsuarioDto Admin(Integer idUsuario);
    UsuarioDto actualizarPassword(String ci, String nuevaContrasena);
    UsuarioDto toggleActive(Integer idUsuario);
    UsuarioDto registerFromGlobal(String nombre, String apellido, String email, String ci, String telefono);


}
