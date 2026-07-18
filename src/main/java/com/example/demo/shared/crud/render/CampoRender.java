package com.example.demo.shared.crud.render;

public interface CampoRender {
    String tipo();
    String rotulo();
    default String name() {
        return null;
    }
}
