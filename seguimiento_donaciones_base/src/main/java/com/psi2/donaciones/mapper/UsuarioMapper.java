package com.psi2.donaciones.mapper;

import com.psi2.donaciones.dto.UsuarioDto;
import com.psi2.donaciones.entities.entitySQL.Usuario;

public class UsuarioMapper {

    public static UsuarioDto toDto(Usuario usuario) {
        return new UsuarioDto(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreoElectronico(),
                usuario.getCi(),
                usuario.getContrasena()
        );
    }

    public static Usuario toEntity(UsuarioDto dto) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setCi(dto.getCi());
        usuario.setContrasena(dto.getContrasena());
        return usuario;
    }
}
