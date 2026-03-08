package com.example.demo.shared.crud.filtros;

import com.example.demo.shared.crud.FiltroCrud;
import com.example.demo.shared.crud.TipoEntradaCrud;

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
