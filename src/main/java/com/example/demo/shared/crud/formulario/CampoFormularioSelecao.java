package com.example.demo.shared.crud.formulario;

import com.example.demo.shared.crud.OpcaoCrud;
import com.example.demo.shared.crud.render.CampoRender;
import com.example.demo.shared.crud.render.CampoSelecaoRender;
import java.util.List;
import java.util.function.Function;

public record CampoFormularioSelecao<T>(
        String nome,
        String rotulo,
        boolean obrigatorio,
        List<OpcaoCrud> opcoes,
        String classeCssCampo,
        Function<T, Object> extratorValor
) implements CampoFormularioCrud<T> {

    @Override
    public String nomeFragmento() {
        return "campoSelecao";
    }

    @Override
    public CampoRender renderizar(T dto) {
        Object valorRaw = extratorValor != null ? extratorValor.apply(dto) : null;
        Object valor = valorRaw;
        if ("revestimento".equals(nome()) && valorRaw instanceof Boolean b) {
            valor = String.valueOf(b);
        }
        return new CampoSelecaoRender(
                rotulo(),
                nome(),
                classeCssCampo(),
                obrigatorio(),
                valor,
                opcoes()
        );
    }
}