package com.example.demo.crud.formulario;

public record CampoFormularioTextarea(
        String nome,
        String rotulo,
        String dica,
        boolean obrigatorio,
        Integer linhas,
        Integer maximoCaracteres,
        String classeCssCampo
) implements CampoFormularioCrud {

    @Override
    public String nomeFragmento() {
        return "campoTextarea";
    }
}
