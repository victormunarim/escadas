package com.example.demo.auth.service;
import com.example.demo.auth.model.UsuarioEntity;
import com.example.demo.auth.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository repositorioUsuario;

    public UsuarioService(UsuarioRepository repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    public UsuarioEntity salvar(UsuarioEntity usuario) {
        return repositorioUsuario.save(usuario);
    }
}