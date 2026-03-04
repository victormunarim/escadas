package com.example.demo.crud;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CrudModuleRegistry {

    private final Map<String, CrudModuleProvider> providers;

    public CrudModuleRegistry(List<CrudModuleProvider> providers) {
        this.providers = providers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        CrudModuleProvider::key,
                        p -> p,
                        (a, b) -> {
                            throw new IllegalStateException("Chave de módulo duplicada: " + a.key());
                        }
                ));
    }

    public CrudModuleProvider getRequired(String key) {
        CrudModuleProvider provider = providers.get(key);
        if (provider == null) throw new IllegalArgumentException("Módulo não existe: " + key);
        return provider;
    }
}