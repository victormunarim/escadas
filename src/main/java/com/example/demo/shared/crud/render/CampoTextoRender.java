package com.example.demo.shared.crud.render;

public record CampoTextoRender(
        String tipo,
        String rotulo,
        String name,
        String classeCampo,
        String type,
        boolean required,
        Integer maxLength,
        String inputMode,
        String placeholder,
        Object value
) implements CampoRender {
    public CampoTextoRender(String rotulo, String name, String classeCampo, String type, boolean required, Integer maxLength, String inputMode, String placeholder, Object value) {
        this("input", rotulo, name, classeCampo, type, required, maxLength, inputMode, placeholder, value);
    }
}
