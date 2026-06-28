package com.example.demo.shared.crud.render;

public record CampoNumeroRender(
        String tipo,
        String type,
        String rotulo,
        String name,
        String classeCampo,
        boolean required,
        String min,
        String max,
        String step,
        Object value
) implements CampoRender {
    public CampoNumeroRender(String rotulo, String name, String classeCampo, boolean required, String min, String max, String step, Object value) {
        this("input", "number", rotulo, name, classeCampo, required, min, max, step, value);
    }
}
