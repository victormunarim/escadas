package com.example.demo.shared.crud.filtros;

import com.example.demo.shared.crud.FiltroCrud;
import com.example.demo.shared.crud.TipoEntradaCrud;

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