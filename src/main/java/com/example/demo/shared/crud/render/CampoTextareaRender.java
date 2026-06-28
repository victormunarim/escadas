package com.example.demo.shared.crud.render;

public record CampoTextareaRender(
        String tipo,
        String rotulo,
        String name,
        String classeCampo,
        boolean required,
        Integer rows,
        Integer maxLength,
        String placeholder,
        Object value
) implements CampoRender {
    public CampoTextareaRender(String rotulo, String name, String classeCampo, boolean required, Integer rows, Integer maxLength, String placeholder, Object value) {
        this("textarea", rotulo, name, classeCampo, required, rows, maxLength, placeholder, value);
    }
}
