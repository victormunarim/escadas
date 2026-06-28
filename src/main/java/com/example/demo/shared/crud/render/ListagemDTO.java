package com.example.demo.shared.crud.render;

import java.util.List;

public record ListagemDTO(
        List<ColunaListagem> colunas,
        List<LinhaListagem> linhas,
        List<CampoRender> filtros
) {}
