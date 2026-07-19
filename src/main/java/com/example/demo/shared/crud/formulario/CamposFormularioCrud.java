package com.example.demo.shared.crud.formulario;

import com.example.demo.shared.crud.OpcaoCrud;
import java.util.List;
import java.util.function.Function;

public final class CamposFormularioCrud {
    private CamposFormularioCrud() {}

    public static <T> CampoFormularioTexto<T> texto(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            Integer maximoCaracteres,
            String classeCssCampo,
            Function<T, Object> extratorValor
    ) {
        return new CampoFormularioTexto<>(
                nome, rotulo, dica, obrigatorio, "text", maximoCaracteres, classeCssCampo, extratorValor
        );
    }

    public static <T> CampoFormularioTexto<T> email(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            Integer maximoCaracteres,
            String classeCssCampo,
            Function<T, Object> extratorValor
    ) {
        return new CampoFormularioTexto<>(
                nome, rotulo, dica, obrigatorio, "email", maximoCaracteres, classeCssCampo, extratorValor
        );
    }

    public static <T> CampoFormularioNumero<T> numero(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            String minimo,
            String maximo,
            String passo,
            String classeCssCampo,
            Function<T, Object> extratorValor
    ) {
        return new CampoFormularioNumero<>(
                nome, rotulo, dica, obrigatorio, minimo, maximo, passo, classeCssCampo, extratorValor
        );
    }

    public static <T> CampoFormularioNumero<T> numero(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            String minimo,
            String passo,
            String classeCssCampo,
            Function<T, Object> extratorValor
    ) {
        return numero(nome, rotulo, dica, obrigatorio, minimo, null, passo, classeCssCampo, extratorValor);
    }

    public static <T> CampoFormularioSelecao<T> selecao(
            String nome,
            String rotulo,
            boolean obrigatorio,
            List<OpcaoCrud> opcoes,
            String classeCssCampo,
            Function<T, Object> extratorValor
    ) {
        return new CampoFormularioSelecao<>(nome, rotulo, obrigatorio, opcoes, classeCssCampo, extratorValor);
    }

    public static <T> CampoFormularioTextarea<T> textarea(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            Integer linhas,
            Integer maximoCaracteres,
            String classeCssCampo,
            Function<T, Object> extratorValor
    ) {
        return new CampoFormularioTextarea<>(
                nome, rotulo, dica, obrigatorio, linhas, maximoCaracteres, classeCssCampo, extratorValor
        );
    }
}