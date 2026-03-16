package com.example.demo.crud.filtros;

import com.example.demo.crud.FiltroCrud;
import com.example.demo.crud.TipoEntradaCrud;

public record FiltroCrudTexto(
        String nome,
        String rotulo,
        String dica
) implements FiltroCrud {

    @Override
    public TipoEntradaCrud tipo() {
        return TipoEntradaCrud.TEXT;
    }

    @Override
    public String nomeFragmento() {
        return "filtroTexto";
    }
}