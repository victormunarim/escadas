package com.example.demo.crud;

public record ColunaCrud(
        String cabecalho,
        String campo
) {
    public static ColunaCrud of(String cabecalho, String campo) {
        return new ColunaCrud(cabecalho, campo);
    }

    public static ColunaCrud col(String cabecalho, String campo) {
        return of(cabecalho, campo);
    }
}