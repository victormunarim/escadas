package com.example.demo.shared.crud.render;

public record SecaoRender(
        String tipo,
        String rotulo
) implements CampoRender {
    public SecaoRender(String rotulo) {
        this("secao", rotulo);
    }
}
