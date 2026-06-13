package com.example.demo.shared.crud.filtros;

import com.example.demo.shared.crud.FiltroCrud;
import com.example.demo.shared.crud.TipoEntradaCrud;
import com.example.demo.shared.crud.OpcaoCrud;

import java.util.List;

public record FiltroCrudSelecao(
        String nome,
        String rotulo,
        List<OpcaoCrud> opcoes
) implements FiltroCrud {

    @Override
    public TipoEntradaCrud tipo() {
        return TipoEntradaCrud.SELECT;
    }

    @Override
    public String nomeFragmento() {
        return "filtroSelecao";
    }

    @Override
    public String dica() {
        return null;
    }
}