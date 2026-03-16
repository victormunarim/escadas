package com.example.demo.service;

import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repositorioUsuario;

    public UsuarioService(UsuarioRepository repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    public Optional<UsuarioEntity> buscarPorLogin(String login) {
        if (login == null || login.isBlank()) {
            return Optional.empty();
        }

        Optional<UsuarioEntity> usuarioPorEmail = repositorioUsuario.findByEmailIgnoreCase(login);
        if (usuarioPorEmail.isPresent()) {
            return usuarioPorEmail;
        }

        return repositorioUsuario.findByNomeIgnoreCase(login);
    }

    public UsuarioEntity salvar(UsuarioEntity usuario) {
        return repositorioUsuario.save(usuario);
    }
}
