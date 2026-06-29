package com.example.demo.shared.crud.render;

import java.util.Map;

public record LinhaListagem(
        Object id,
        Map<String, Object> valores
) {}
