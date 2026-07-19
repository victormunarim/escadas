package com.example.demo.shared.crud.service;

public interface CommandService<TFormDTO, ID> {
    ID salvarFormulario(TFormDTO formulario);
    void atualizarFormulario(ID id, TFormDTO formulario);
    void excluir(ID id);
}
