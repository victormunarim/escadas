package com.example.demo.shared.crud.formulario;

import com.example.demo.shared.crud.render.CampoNumeroRender;
import com.example.demo.shared.crud.render.CampoRender;
import java.util.function.Function;

public record CampoFormularioNumero<T>(
        String nome,
        String rotulo,
        String dica,
        boolean obrigatorio,
        String minimo,
        String maximo,
        String passo,
        String classeCssCampo,
        Function<T, Object> extratorValor
) implements CampoFormularioCrud<T> {

    @Override
    public String nomeFragmento() {
        return "campoNumero";
    }

    @Override
    public CampoRender renderizar(T dto) {
        Object valor = extratorValor != null ? extratorValor.apply(dto) : null;
        return new CampoNumeroRender(
                rotulo(),
                nome(),
                classeCssCampo(),
                obrigatorio(),
                minimo(),
                maximo(),
                passo(),
                valor
        );
    }
}