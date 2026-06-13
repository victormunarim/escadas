package com.example.demo.shared.crud;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RegistroModuloCrud {

    private final Map<String, ProvedorModuloCrud> provedores;

    public RegistroModuloCrud(List<ProvedorModuloCrud> provedores) {
        this.provedores = provedores.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ProvedorModuloCrud::chave,
                        p -> p,
                        (a, b) -> {
                            throw new IllegalStateException("Chave de módulo duplicada: " + a.chave());
                        }
                ));
    }

    public ProvedorModuloCrud obterObrigatorio(String chave) {
        ProvedorModuloCrud provedor = provedores.get(chave);
        if (provedor == null) throw new IllegalArgumentException("Módulo não existe: " + chave);
        return provedor;
    }
}