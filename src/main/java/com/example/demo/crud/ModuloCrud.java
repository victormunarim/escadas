package com.example.demo.crud;

import java.util.List;

public record ModuloCrud(
        String chave,
        String titulo,
        String urlAcao,
        List<FiltroCrud> filtros,
        List<ColunaCrud> colunas
) {}