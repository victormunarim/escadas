package com.example.demo.auth.security;

public record UsuarioAutenticado(
        Long id,
        String nome,
        String email
) {
}
