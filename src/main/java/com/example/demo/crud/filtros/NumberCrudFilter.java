package com.example.demo.crud.filtros;

import com.example.demo.crud.CrudFilter;
import com.example.demo.crud.CrudInputType;

public record NumberCrudFilter(
        String name,
        String label,
        String placeholder
) implements CrudFilter {

    @Override
    public CrudInputType type() {
        return CrudInputType.NUMBER;
    }

    @Override
    public String fragmentName() {
        return "numberFilter";
    }
}