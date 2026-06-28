package com.example.demo.shared.crud.listagem;

import com.example.demo.shared.crud.render.ColunaListagem;
import java.util.function.Function;

public record ColunaConfig<T>(
        String chave,
        String label,
        Function<T, Object> extratorValor
) {
    public ColunaListagem toColunaListagem() {
        return new ColunaListagem(chave, label);
    }

    public Object extrairValor(T dto) {
        return extratorValor != null ? extratorValor.apply(dto) : null;
    }
}
