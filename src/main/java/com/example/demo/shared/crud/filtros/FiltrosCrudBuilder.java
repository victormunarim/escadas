package com.example.demo.shared.crud.filtros;

import com.example.demo.shared.crud.FiltroCrud;
import com.example.demo.shared.crud.OpcaoCrud;

import java.util.ArrayList;
import java.util.List;

public final class FiltrosCrudBuilder {
    private final List<FiltroCrud> filtros = new ArrayList<>();

    private FiltrosCrudBuilder() {}

    public static FiltrosCrudBuilder criar() {
        return new FiltrosCrudBuilder();
    }

    public FiltrosCrudBuilder texto(String nome, String rotulo, String dica) {
        filtros.add(FiltrosCrud.text(nome, rotulo, dica));
        return this;
    }

    public FiltrosCrudBuilder data(String nome, String rotulo, String dica) {
        filtros.add(FiltrosCrud.date(nome, rotulo, dica));
        return this;
    }

    public FiltrosCrudBuilder selecao(String nome, String rotulo, List<OpcaoCrud> opcoes) {
        filtros.add(FiltrosCrud.select(nome, rotulo, opcoes));
        return this;
    }

    public FiltrosCrudBuilder numero(String nome, String rotulo, String dica) {
        filtros.add(FiltrosCrud.numeroExato(nome, rotulo, dica));
        return this;
    }

    public List<FiltroCrud> build() {
        return List.copyOf(filtros);
    }
}
