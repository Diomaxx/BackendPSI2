package com.psi2.donaciones.service.serviceimpl;

import com.psi2.donaciones.auth.AuthRequest;
import com.psi2.donaciones.auth.AuthResponse;
import com.psi2.donaciones.entities.entitySQL.Usuario;
import com.psi2.donaciones.repository.UsuarioRepository;
import com.psi2.donaciones.config.JwtUtil;
import com.psi2.donaciones.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponse login(AuthRequest request) {
        Usuario usuario = usuarioRepository.findByCi(request.getCedulaIdentidad())
                .orElseThrow(() -> new BadCredentialsException("CI o contraseña incorrectos"));

        if (!usuario.getActive()) {
            throw new BadCredentialsException("El usuario no está activo. Registrate o Solicita tu activación");
        }

        if (request.getContrasena() == null || request.getContrasena().isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new BadCredentialsException("CI o contraseña incorrectos");
        }

        var userDetails = User.withUsername(usuario.getCi())
                .password(usuario.getContrasena())
                .authorities("USER")
                .build();

        String token = jwtUtil.generateToken(userDetails);
        long expiration = jwtUtil.getExpirationDate().getTime();

        return new AuthResponse(token, expiration);
    }


}
