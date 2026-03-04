package com.example.demo.crud;

import java.util.List;

public record CrudModule(
        String key,
        String title,
        String actionUrl,
        List<CrudFilter> filters,
        List<CrudColumn> columns
) {}