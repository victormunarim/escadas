package com.example.demo.pedidos;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EspecificacaoPedido {

    public static Specification<Pedido> filtro(String busca, String numeroBusca) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(
                    cb.or(
                            cb.isFalse(root.get("oculto")),
                            cb.isNull(root.get("oculto"))
                    )
            );

            if (numeroBusca != null && !numeroBusca.isBlank()) {
                try {
                    Integer numero = Integer.parseInt(numeroBusca);
                    predicates.add(
                            cb.equal(root.get("numeroPedido"), numero)
                    );
                } catch (NumberFormatException ignored) {}
            }

            if (busca != null && !busca.isBlank()) {

                String like = "%" + busca.toLowerCase() + "%";

                List<Predicate> orPredicates = new ArrayList<>();

                orPredicates.add(cb.like(cb.lower(root.get("nomeCliente")), like));
                orPredicates.add(cb.like(cb.lower(root.get("email")), like));
                orPredicates.add(cb.like(cb.lower(root.get("cidade")), like));
                orPredicates.add(cb.like(cb.lower(root.get("bairro")), like));
                orPredicates.add(cb.like(cb.lower(root.get("descricao")), like));
                orPredicates.add(cb.like(cb.lower(root.get("cliente")), like));

                predicates.add(
                        cb.or(orPredicates.toArray(new Predicate[0]))
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
