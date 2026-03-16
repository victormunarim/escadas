package com.example.demo.crud.formulario;

import com.example.demo.crud.OpcaoCrud;

import java.util.List;

public final class CamposFormularioCrud {
    private CamposFormularioCrud() {}

    public static CampoFormularioTexto texto(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            Integer maximoCaracteres,
            String classeCssCampo
    ) {
        return new CampoFormularioTexto(nome, rotulo, dica, obrigatorio, "text", maximoCaracteres, classeCssCampo);
    }

    public static CampoFormularioTexto email(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            Integer maximoCaracteres,
            String classeCssCampo
    ) {
        return new CampoFormularioTexto(nome, rotulo, dica, obrigatorio, "email", maximoCaracteres, classeCssCampo);
    }

    public static CampoFormularioNumero numero(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            String minimo,
            String maximo,
            String passo,
            String classeCssCampo
    ) {
        return new CampoFormularioNumero(nome, rotulo, dica, obrigatorio, minimo, maximo, passo, classeCssCampo);
    }

    public static CampoFormularioNumero numero(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            String minimo,
            String passo,
            String classeCssCampo
    ) {
        return numero(nome, rotulo, dica, obrigatorio, minimo, null, passo, classeCssCampo);
    }

    public static CampoFormularioSelecao selecao(
            String nome,
            String rotulo,
            boolean obrigatorio,
            List<OpcaoCrud> opcoes,
            String classeCssCampo
    ) {
        return new CampoFormularioSelecao(nome, rotulo, obrigatorio, opcoes, classeCssCampo);
    }

    public static CampoFormularioTextarea textarea(
            String nome,
            String rotulo,
            String dica,
            boolean obrigatorio,
            Integer linhas,
            Integer maximoCaracteres,
            String classeCssCampo
    ) {
        return new CampoFormularioTextarea(nome, rotulo, dica, obrigatorio, linhas, maximoCaracteres, classeCssCampo);
    }
}
