package com.example.demo.shared.crud.formulario;

import com.example.demo.shared.crud.render.CampoRender;
import com.example.demo.shared.crud.render.CampoTextoRender;
import java.util.function.Function;

public record CampoFormularioTexto<T>(
        String nome,
        String rotulo,
        String dica,
        boolean obrigatorio,
        String tipoHtml,
        Integer maximoCaracteres,
        String classeCssCampo,
        Function<T, Object> extratorValor
) implements CampoFormularioCrud<T> {

    @Override
    public String nomeFragmento() {
        return "campoTexto";
    }

    @Override
    public CampoRender renderizar(T dto) {
        Object valor = extratorValor != null ? extratorValor.apply(dto) : null;
        return new CampoTextoRender(
                rotulo(),
                nome(),
                classeCssCampo(),
                tipoHtml(),
                obrigatorio(),
                maximoCaracteres(),
                obterInputMode(nome()),
                obterPlaceholder(nome()),
                valor
        );
    }

    private String obterInputMode(String nomeCampo) {
        if ("cpf".equals(nomeCampo) || "rg".equals(nomeCampo) || "telefone".equals(nomeCampo) || "telefoneFixo".equals(nomeCampo) || "numero".equals(nomeCampo) || "numeroCliente".equals(nomeCampo)) {
            return "numeric";
        }
        return null;
    }

    private String obterPlaceholder(String nomeCampo) {
        if ("cpf".equals(nomeCampo) || "rg".equals(nomeCampo)) {
            return "Somente números";
        }
        return null;
    }
}