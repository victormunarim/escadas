package com.example.demo.shared.crud.formulario;

import com.example.demo.shared.crud.render.CampoRender;
import com.example.demo.shared.crud.render.CampoTextareaRender;
import java.util.function.Function;

public record CampoFormularioTextarea<T>(
        String nome,
        String rotulo,
        String dica,
        boolean obrigatorio,
        Integer linhas,
        Integer maximoCaracteres,
        String classeCssCampo,
        Function<T, Object> extratorValor
) implements CampoFormularioCrud<T> {

    @Override
    public String nomeFragmento() {
        return "campoTextarea";
    }

    @Override
    public CampoRender renderizar(T dto) {
        Object valor = extratorValor != null ? extratorValor.apply(dto) : null;
        return new CampoTextareaRender(
                rotulo(),
                nome(),
                classeCssCampo(),
                obrigatorio(),
                linhas(),
                maximoCaracteres(),
                "Detalhes específicos do pedido...",
                valor
        );
    }
}