package com.example.demo.crud.filtros;

import com.example.demo.crud.OpcaoCrud;

import java.util.List;

public final class FiltrosCrud {
    public static FiltroCrudTexto text(String nome, String rotulo, String dica) {
        return new FiltroCrudTexto(nome, rotulo, dica);
    }

    public static FiltroCrudData date(String nome, String rotulo, String dica) {
        return new FiltroCrudData(nome, rotulo, dica);
    }

    public static FiltroCrudSelecao select(String nome, String rotulo, List<OpcaoCrud> opcoes) {
        return new FiltroCrudSelecao(nome, rotulo, opcoes);
    }

    public static FiltroCrudNumeroExato numeroExato(String nome, String rotulo, String dica) {
        return new FiltroCrudNumeroExato(nome, rotulo, dica);
    }
}
