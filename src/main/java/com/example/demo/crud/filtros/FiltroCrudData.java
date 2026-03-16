package com.example.demo.crud.filtros;

import com.example.demo.crud.FiltroCrud;
import com.example.demo.crud.TipoEntradaCrud;

public record FiltroCrudData(
        String nome,
        String rotulo,
        String dica
) implements FiltroCrud {

    @Override
    public TipoEntradaCrud tipo() {
        return TipoEntradaCrud.DATE;
    }

    @Override
    public String nomeFragmento() {
        return "filtroData";
    }
}
