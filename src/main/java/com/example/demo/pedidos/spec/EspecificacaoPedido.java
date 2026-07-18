package com.example.demo.pedidos.spec;

import com.example.demo.orcamentos.model.OrcamentoEntity;
import com.example.demo.pedidos.config.ColunasPedido;
import com.example.demo.pedidos.model.PedidoEntity;
import com.example.demo.shared.crud.filtros.compostos.FiltroNumeroExato;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

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
            String temOrcamento
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.or(
                            cb.isFalse(root.get(ColunasPedido.CAMPO_FLAG_OCULTO)),
                            cb.isNull(root.get(ColunasPedido.CAMPO_FLAG_OCULTO))
                    )
            );

            Optional<Integer> numero = FiltroNumeroExato.parse(numeroBusca);
            numero.ifPresent(valor ->
                    predicates.add(cb.equal(root.get(ColunasPedido.CAMPO_NUMERO_PEDIDO), valor))
            );

            if (busca != null && !busca.isBlank()) {
                String like = "%" + busca.trim().toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("clienteNome")), like),
                                cb.like(cb.lower(root.get(ColunasPedido.CAMPO_EMAIL)), like),
                                cb.like(root.get(ColunasPedido.CAMPO_DESCRICAO), like)
                        )
                );
            }

            Integer mesValor = parseInt(mes);
            Integer anoValor = parseInt(ano);
            Integer diaValor = parseInt(dia);

            if (anoValor != null && mesValor != null) {
                predicates.add(cb.equal(cb.function("year", Integer.class, root.get(ColunasPedido.CAMPO_DATA_CADASTRO)), anoValor));
                predicates.add(cb.equal(cb.function("month", Integer.class, root.get(ColunasPedido.CAMPO_DATA_CADASTRO)), mesValor));
                if (diaValor != null) {
                    predicates.add(cb.equal(cb.function("day", Integer.class, root.get(ColunasPedido.CAMPO_DATA_CADASTRO)), diaValor));
                }
            } else if (anoValor != null) {
                predicates.add(cb.equal(cb.function("year", Integer.class, root.get(ColunasPedido.CAMPO_DATA_CADASTRO)), anoValor));
            }

            if ("true".equalsIgnoreCase(temOrcamento)) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<OrcamentoEntity> orcamentoRoot = subquery.from(OrcamentoEntity.class);
                subquery.select(orcamentoRoot.get("id"));
                subquery.where(
                        cb.equal(orcamentoRoot.get("pedido").get("id"), root.get("id")),
                        cb.or(cb.isFalse(orcamentoRoot.get("flagOculto")), cb.isNull(orcamentoRoot.get("flagOculto")))
                );
                predicates.add(cb.exists(subquery));
            } else if ("false".equalsIgnoreCase(temOrcamento)) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<OrcamentoEntity> orcamentoRoot = subquery.from(OrcamentoEntity.class);
                subquery.select(orcamentoRoot.get("id"));
                subquery.where(
                        cb.equal(orcamentoRoot.get("pedido").get("id"), root.get("id")),
                        cb.or(cb.isFalse(orcamentoRoot.get("flagOculto")), cb.isNull(orcamentoRoot.get("flagOculto")))
                );
                predicates.add(cb.not(cb.exists(subquery)));
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