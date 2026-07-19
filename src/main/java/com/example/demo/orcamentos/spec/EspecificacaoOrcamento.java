package com.example.demo.orcamentos.spec;

import com.example.demo.orcamentos.model.OrcamentoEntity;
import com.example.demo.shared.util.NumeroUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EspecificacaoOrcamento {

    public static Specification<OrcamentoEntity> filtro(
            String busca,
            String dia,
            String mes,
            String ano,
            String encerrado,
            String etiquetaId,
            String tecnico
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.or(
                            cb.isFalse(root.get("flagOculto")),
                            cb.isNull(root.get("flagOculto"))
                    )
            );

            if ("true".equalsIgnoreCase(tecnico)) {
                predicates.add(cb.isNotNull(root.get("pedido")));
            } else if ("false".equalsIgnoreCase(tecnico)) {
                predicates.add(cb.isNull(root.get("pedido")));
            }

            if (busca != null && !busca.isBlank()) {
                String like = "%" + busca.trim().toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("nome")), like),
                                cb.like(cb.lower(root.get("bairro")), like),
                                cb.like(cb.lower(root.get("descricao")), like)
                        )
                );
            }

            if (etiquetaId != null && !etiquetaId.isBlank()) {
                try {
                    Long idEtiqueta = Long.parseLong(etiquetaId.trim());
                    predicates.add(cb.equal(root.get("etiqueta").get("id"), idEtiqueta));
                } catch (NumberFormatException ignored) {
                }
            }

            Integer mesValor = NumeroUtil.parseIntSeguro(mes);
            Integer anoValor = NumeroUtil.parseIntSeguro(ano);
            Integer diaValor = NumeroUtil.parseIntSeguro(dia);

            if (anoValor != null && mesValor != null) {
                predicates.add(cb.equal(cb.function("year", Integer.class, root.get("dataCadastro")), anoValor));
                predicates.add(cb.equal(cb.function("month", Integer.class, root.get("dataCadastro")), mesValor));
                if (diaValor != null) {
                    predicates.add(cb.equal(cb.function("day", Integer.class, root.get("dataCadastro")), diaValor));
                }
            } else if (anoValor != null) {
                predicates.add(cb.equal(cb.function("year", Integer.class, root.get("dataCadastro")), anoValor));
            }

            if ("true".equalsIgnoreCase(encerrado)) {
                predicates.add(cb.isTrue(root.get("flagEncerrado")));
            } else if ("false".equalsIgnoreCase(encerrado)) {
                predicates.add(
                        cb.or(
                                cb.isFalse(root.get("flagEncerrado")),
                                cb.isNull(root.get("flagEncerrado"))
                        )
                );
            }

            if (!Long.class.equals(query.getResultType()) && !Long.TYPE.equals(query.getResultType())) {
                Join<Object, Object> etiquetaJoin = root.join("etiqueta", JoinType.LEFT);
                query.orderBy(
                        cb.asc(
                                cb.selectCase()
                                        .when(cb.equal(cb.lower(etiquetaJoin.get("nome")), "riscar"), 1)
                                        .when(cb.equal(cb.lower(etiquetaJoin.get("nome")), "aguardando aprovação"), 2)
                                        .when(cb.equal(cb.lower(etiquetaJoin.get("nome")), "aguardando aprovacao"), 2)
                                        .when(cb.equal(cb.lower(etiquetaJoin.get("nome")), "3d / renderizar"), 3)
                                        .when(cb.equal(cb.lower(etiquetaJoin.get("nome")), "proposta"), 4)
                                        .otherwise(5)
                        ),
                        cb.desc(root.get("id"))
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
