package com.example.demo.shared.crud.service;

import com.example.demo.shared.crud.render.*;
import java.util.List;
import java.util.Map;

public interface CrudService<TFormDTO> {
    ListagemDTO listarResumo(Map<String, String> parametros);
    List<CampoRender> obterCamposRenderNovo();
    List<CampoRender> obterCamposRenderEdicao(Long id);
    void salvarFormulario(TFormDTO formulario);
    void atualizarFormulario(Long id, TFormDTO formulario);
    void excluir(Long id);
}
