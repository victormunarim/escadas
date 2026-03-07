package com.example.demo.crud.filtros;

import com.example.demo.crud.CrudOption;

import java.util.List;

public final class Filters {
    public static TextCrudFilter text(String name, String label, String placeholder) {
        return new TextCrudFilter(name, label, placeholder);
    }

    public static SelectCrudFilter select(String name, String label, List<CrudOption> options) {
        return new SelectCrudFilter(name, label, options);
    }
}