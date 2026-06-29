package com.example.demo.orcamentos.spec;

import com.example.demo.orcamentos.model.OrcamentoEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EspecificacaoOrcamento {

    public static Specification<OrcamentoEntity> filtro(
            String busca,
            String dia,
            String mes,
            String ano,
            String encerrado
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.or(
                            cb.isFalse(root.get("flagOculto")),
                            cb.isNull(root.get("flagOculto"))
                    )
            );

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
