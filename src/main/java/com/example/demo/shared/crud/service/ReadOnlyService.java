package com.example.demo.shared.crud.service;

import com.example.demo.shared.crud.render.ListagemDTO;
import java.util.Map;

public interface ReadOnlyService<ID> {
    ListagemDTO listarResumo(Map<String, String> parametros);
}
