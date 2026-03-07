package com.example.demo.crud.filtros;

import com.example.demo.crud.CrudFilter;
import com.example.demo.crud.CrudInputType;
import com.example.demo.crud.CrudOption;

import java.util.List;

public record SelectCrudFilter(
        String name,
        String label,
        List<CrudOption> options
) implements CrudFilter {

    @Override
    public CrudInputType type() {
        return CrudInputType.SELECT;
    }

    @Override
    public String fragmentName() {
        return "selectFilter";
    }

    @Override
    public String placeholder() {
        return null;
    }
}