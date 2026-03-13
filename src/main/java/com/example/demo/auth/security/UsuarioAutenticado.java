package com.example.demo.auth.security;

import java.io.Serial;
import java.io.Serializable;

public record UsuarioAutenticado(
        Long id,
        String nome,
        String email
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
