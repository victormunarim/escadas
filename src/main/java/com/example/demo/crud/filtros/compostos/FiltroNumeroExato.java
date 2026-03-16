package com.example.demo.crud.filtros.compostos;

import java.util.Optional;

public final class FiltroNumeroExato {

    private FiltroNumeroExato() {}

    public static Optional<Integer> parse(String valor) {
        if (valor == null || valor.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(valor.trim()));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }
}
