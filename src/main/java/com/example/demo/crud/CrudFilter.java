package com.example.demo.crud;

import java.util.List;

public interface CrudFilter {

    String name();

    String label();

    CrudInputType type();

    String fragmentName();

    default String placeholder() {
        return null;
    }

    default List<CrudOption> options() {
        return List.of();
    }
}