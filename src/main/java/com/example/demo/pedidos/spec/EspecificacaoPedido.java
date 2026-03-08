package com.example.demo.pedidos.spec;

import com.example.demo.pedidos.model.Pedido;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class EspecificacaoPedido {

    public static Specification<Pedido> filtro(
            String busca,
            String numeroBusca,
            String dataInicio,
            String dataFim
    ) {

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
                orPredicates.add(cb.like(cb.lower(root.get("municipio")), like));
                orPredicates.add(cb.like(cb.lower(root.get("bairro")), like));
                orPredicates.add(cb.like(cb.lower(root.get("descricao")), like));

                predicates.add(
                        cb.or(orPredicates.toArray(new Predicate[0]))
                );
            }

            LocalDate inicio = parseData(dataInicio);
            if (inicio != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("dataCadastro"), inicio.atStartOfDay())
                );
            }

            LocalDate fim = parseData(dataFim);
            if (fim != null) {
                LocalDateTime inicioProximoDia = fim.plusDays(1).atStartOfDay();
                predicates.add(
                        cb.lessThan(root.get("dataCadastro"), inicioProximoDia)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static LocalDate parseData(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(valor.trim());
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }
}
