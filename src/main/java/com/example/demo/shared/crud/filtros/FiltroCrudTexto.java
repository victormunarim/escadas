package com.example.demo.shared.crud.filtros;

import com.example.demo.shared.crud.FiltroCrud;
import com.example.demo.shared.crud.TipoEntradaCrud;

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