package com.example.demo.filtros;

import com.example.demo.crud.CrudOption;

import java.util.List;

public record CrudFilter(
        String name,
        String label,
        CrudInputType type,
        String placeholder,
        List<CrudOption> options
) {
    public static CrudFilter text(String name, String label, String placeholder) {
        return new CrudFilter(name, label, CrudInputType.TEXT, placeholder, null);
    }

    public static CrudFilter number(String name, String label, String placeholder) {
        return new CrudFilter(name, label, CrudInputType.NUMBER, placeholder, null);
    }

    public static CrudFilter date(String name, String label, String placeholder) {
        return new CrudFilter(name, label, CrudInputType.DATE, placeholder, null);
    }

    public static CrudFilter select(String name, String label, List<CrudOption> options) {
        return new CrudFilter(name, label, CrudInputType.SELECT, null, options);
    }
}