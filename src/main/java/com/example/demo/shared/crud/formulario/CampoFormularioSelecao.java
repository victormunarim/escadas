package com.example.demo.shared.crud.formulario;

import com.example.demo.shared.crud.OpcaoCrud;

import java.util.List;

public record CampoFormularioSelecao(
        String nome,
        String rotulo,
        boolean obrigatorio,
        List<OpcaoCrud> opcoes,
        String classeCssCampo
) implements CampoFormularioCrud {

    @Override
    public String nomeFragmento() {
        return "campoSelecao";
    }
}
