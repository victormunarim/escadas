package com.example.demo.shared.crud.filtros;

import com.example.demo.shared.crud.OpcaoCrud;

import java.util.List;

public final class FiltrosCrud {
    public static FiltroCrudTexto text(String nome, String rotulo, String dica) {
        return new FiltroCrudTexto(nome, rotulo, dica);
    }

    public static FiltroCrudSelecao select(String nome, String rotulo, List<OpcaoCrud> opcoes) {
        return new FiltroCrudSelecao(nome, rotulo, opcoes);
    }
}