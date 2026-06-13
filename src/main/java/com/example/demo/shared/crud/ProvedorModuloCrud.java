package com.example.demo.shared.crud;

import java.util.List;
import java.util.Map;

public interface ProvedorModuloCrud {
    String chave();
    ModuloCrud modulo();

    List<Map<String, Object>> linhas(Map<String, String> parametros);
}