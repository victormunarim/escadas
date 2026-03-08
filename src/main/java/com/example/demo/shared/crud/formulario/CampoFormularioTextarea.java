package com.example.demo.shared.crud.formulario;

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
