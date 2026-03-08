package com.example.demo.shared.crud.formulario;

public record CampoFormularioNumero(
        String nome,
        String rotulo,
        String dica,
        boolean obrigatorio,
        String minimo,
        String maximo,
        String passo,
        String classeCssCampo
) implements CampoFormularioCrud {

    @Override
    public String nomeFragmento() {
        return "campoNumero";
    }
}
