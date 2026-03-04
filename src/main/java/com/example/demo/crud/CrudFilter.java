package com.example.demo.crud;

import java.util.List;

public record CrudFilter(
        String name,              // ex: "q" | "status"
        String label,             // ex: "Pesquisar" | "Status"
        CrudInputType type,       // TEXT, SELECT
        String placeholder,       // só para TEXT (pode ser null)
        List<CrudOption> options  // só para SELECT (pode ser null)
) {
    public static CrudFilter text(String name, String label, String placeholder) {
        return new CrudFilter(name, label, CrudInputType.TEXT, placeholder, null);
    }

    public static CrudFilter select(String name, String label, List<CrudOption> options) {
        return new CrudFilter(name, label, CrudInputType.SELECT, null, options);
    }
}