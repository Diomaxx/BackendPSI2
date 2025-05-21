package com.psi2.donaciones.controller;

import com.psi2.donaciones.dto.UsuarioDto;
import com.psi2.donaciones.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
