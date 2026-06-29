package com.example.demo.pedidos.spec;

import com.example.demo.pedidos.model.PedidoEntity;
import com.example.demo.shared.crud.filtros.compostos.FiltroNumeroExato;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EspecificacaoPedido {

    public static Specification<PedidoEntity> filtro(
            String busca,
            String numeroBusca,
            String dia,
            String mes,
            String ano,
            String comOrcamento
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.or(
                            cb.isFalse(root.get("flagOculto")),
                            cb.isNull(root.get("flagOculto"))
                    )
            );

            Optional<Integer> numero = FiltroNumeroExato.parse(numeroBusca);
            numero.ifPresent(valor ->
                    predicates.add(cb.equal(root.get("numeroPedido"), valor))
            );

            if (busca != null && !busca.isBlank()) {
                String like = "%" + busca.trim().toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("clienteNome")), like),
                                cb.like(cb.lower(root.get("email")), like),
                                cb.like(root.get("descricao"), like)
                        )
                );
            }

            Integer mesValor = parseInt(mes);
            Integer anoValor = parseInt(ano);
            Integer diaValor = parseInt(dia);

            if (anoValor != null && mesValor != null) {
                predicates.add(cb.equal(cb.function("year", Integer.class, root.get("dataCadastro")), anoValor));
                predicates.add(cb.equal(cb.function("month", Integer.class, root.get("dataCadastro")), mesValor));
                if (diaValor != null) {
                    predicates.add(cb.equal(cb.function("day", Integer.class, root.get("dataCadastro")), diaValor));
                }
            } else if (anoValor != null) {
                predicates.add(cb.equal(cb.function("year", Integer.class, root.get("dataCadastro")), anoValor));
            }

            if ("true".equalsIgnoreCase(comOrcamento)) {
                predicates.add(cb.isNotNull(root.get("orcamento")));
            } else if ("false".equalsIgnoreCase(comOrcamento)) {
                predicates.add(cb.isNull(root.get("orcamento")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Integer parseInt(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}