package com.example.demo.crud.filtros;

import com.example.demo.crud.FiltroCrud;
import com.example.demo.crud.TipoEntradaCrud;

public record FiltroCrudNumero(
        String nome,
        String rotulo,
        String dica
) implements FiltroCrud {

    @Override
    public TipoEntradaCrud tipo() {
        return TipoEntradaCrud.NUMBER;
    }

    @Override
    public String nomeFragmento() {
        return "filtroNumero";
    }
}