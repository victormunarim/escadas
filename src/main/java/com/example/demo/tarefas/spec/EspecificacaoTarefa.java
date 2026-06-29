package com.example.demo.tarefas.spec;

import com.example.demo.tarefas.model.TarefaEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EspecificacaoTarefa {

    public static Specification<TarefaEntity> filtro(
            String busca,
            String responsavel,
            String concluida,
            String dia,
            String mes,
            String ano
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
                                cb.like(cb.lower(root.get("tipoTarefa").get("nome")), like),
                                cb.like(cb.lower(root.get("responsavel").get("login")), like),
                                cb.like(cb.lower(root.get("extChave")), like)
                        )
                );
            }

            if (responsavel != null && !responsavel.isBlank()) {
                predicates.add(cb.equal(root.get("responsavel").get("login"), responsavel.trim()));
            }

            if (concluida != null && !concluida.isBlank()) {
                boolean isConcluida = Boolean.parseBoolean(concluida.trim());
                predicates.add(cb.equal(root.get("flagConcluida"), isConcluida));
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
