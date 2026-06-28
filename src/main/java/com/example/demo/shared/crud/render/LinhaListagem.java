package com.example.demo.shared.crud.render;

import java.util.Map;

public record LinhaListagem(
        Integer id,
        Map<String, Object> valores
) {}
