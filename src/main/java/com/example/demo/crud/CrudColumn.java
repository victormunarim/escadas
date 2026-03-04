package com.example.demo.crud;

public record CrudColumn(
        String header,
        String field
) {
    public static CrudColumn of(String header, String field) {
        return new CrudColumn(header, field);
    }

    public static CrudColumn col(String header, String field) {
        return of(header, field);
    }
}