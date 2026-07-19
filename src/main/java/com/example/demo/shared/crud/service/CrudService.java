package com.example.demo.shared.crud.service;

public interface CrudService<TFormDTO> extends
        ReadOnlyService<Long>,
        CommandService<TFormDTO, Long>,
        FormSchemaProvider<Long> {
}
