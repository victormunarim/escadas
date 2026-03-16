package com.example.demo.spec;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class BuscaSpecification {

    private BuscaSpecification() {}

    public static <T> Specification<T> textoLike(String termo, List<String> campos) {
        if (termo == null || termo.isBlank() || campos == null || campos.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }

        String like = "%" + termo.toLowerCase() + "%";

        return (root, query, cb) -> {
            List<Predicate> orPredicates = new ArrayList<>();
            for (String campo : campos) {
                orPredicates.add(cb.like(cb.lower(root.get(campo)), like));
            }
            return cb.or(orPredicates.toArray(new Predicate[0]));
        };
    }
}
