package com.example.demo.shared.crud.service;

import com.example.demo.shared.crud.render.CampoRender;
import java.util.List;

public interface FormSchemaProvider<ID> {
    List<CampoRender> obterCamposRenderNovo();
    List<CampoRender> obterCamposRenderEdicao(ID id);
}
