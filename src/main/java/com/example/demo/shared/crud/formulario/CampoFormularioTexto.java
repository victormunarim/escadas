package com.example.demo.shared.crud.formulario;

public record CampoFormularioTexto(
        String nome,
        String rotulo,
        String dica,
        boolean obrigatorio,
        String tipoHtml,
        Integer maximoCaracteres,
        String classeCssCampo
) implements CampoFormularioCrud {

    @Override
    public String nomeFragmento() {
        return "campoTexto";
    }
}
