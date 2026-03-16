package com.example.demo.crud;

import java.util.List;

public interface FiltroCrud {

    String nome();

    String rotulo();

    TipoEntradaCrud tipo();

    String nomeFragmento();

    default String dica() {
        return null;
    }

    default List<OpcaoCrud> opcoes() {
        return List.of();
    }
}