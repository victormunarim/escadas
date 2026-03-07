package com.example.demo.crud.filtros;

import com.example.demo.crud.CrudFilter;
import com.example.demo.crud.CrudInputType;

public record TextCrudFilter(
        String name,
        String label,
        String placeholder
) implements CrudFilter {

    @Override
    public CrudInputType type() {
        return CrudInputType.TEXT;
    }

    @Override
    public String fragmentName() {
        return "textFilter";
    }
}