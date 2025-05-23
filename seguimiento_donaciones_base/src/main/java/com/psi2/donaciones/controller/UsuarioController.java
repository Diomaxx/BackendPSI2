package com.psi2.donaciones.controller;

import com.psi2.donaciones.dto.RegistroGlobalDto;
import com.psi2.donaciones.dto.UsuarioDto;
import com.psi2.donaciones.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioDto> registerUsuario(@RequestBody UsuarioDto usuarioDto) {
        UsuarioDto creado = usuarioService.createUsuario(usuarioDto);
        return ResponseEntity.ok(creado);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
        List<UsuarioDto> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/registro-global")
    public ResponseEntity<UsuarioDto> registerFromGlobal(@RequestBody RegistroGlobalDto dto) {
        UsuarioDto usuario = usuarioService.registerFromGlobal(
                dto.getNombre(),
                dto.getApellido(),
                dto.getEmail(),
                dto.getCi(),
                dto.getPassword(),
                dto.getTelefono()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }


}
