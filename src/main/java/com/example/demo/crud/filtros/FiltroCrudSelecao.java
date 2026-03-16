package com.example.demo.crud.filtros;

import com.example.demo.crud.FiltroCrud;
import com.example.demo.crud.TipoEntradaCrud;
import com.example.demo.crud.OpcaoCrud;

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