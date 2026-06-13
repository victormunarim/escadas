package com.example.demo.shared.config;

import com.example.demo.auth.model.UsuarioEntity;
import com.example.demo.auth.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevDatabaseInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDatabaseInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        usuarioRepository.findByLogin("escadas").ifPresentOrElse(usuario -> {
            boolean senhaCorreta = passwordEncoder.matches("escadas", usuario.getSenha());
            boolean emailPreenchido = usuario.getEmail() != null && !usuario.getEmail().isBlank();

            if (!senhaCorreta) {
                usuario.setSenha(passwordEncoder.encode("escadas"));
            }

            if (!emailPreenchido) {
                usuario.setEmail("escadas@local");
            }

            usuarioRepository.save(usuario);
        }, () -> {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setId(1L);
            usuario.setLogin("escadas");
            usuario.setSenha(passwordEncoder.encode("escadas"));
            usuario.setEmail("escadas@local");
            usuarioRepository.save(usuario);
        });
    }
}