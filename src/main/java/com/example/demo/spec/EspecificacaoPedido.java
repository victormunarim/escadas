package com.example.demo.spec;

import com.example.demo.constants.ColunasPedido;
import com.example.demo.entity.PedidoEntity;
import com.example.demo.crud.filtros.compostos.FiltroNumeroExato;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EspecificacaoPedido {

    private static final List<String> CAMPOS_BUSCA = List.of(
            ColunasPedido.CAMPO_CLIENTE_NOME,
            ColunasPedido.CAMPO_EMAIL,
            ColunasPedido.CAMPO_MUNICIPIO,
            ColunasPedido.CAMPO_BAIRRO,
            ColunasPedido.CAMPO_DESCRICAO
    );

    public static Specification<PedidoEntity> filtro(
            String busca,
            String numeroBusca,
            String dia,
            String mes,
            String ano
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

            Predicate buscaPredicate = BuscaSpecification
                    .<PedidoEntity>textoLike(busca, CAMPOS_BUSCA)
                    .toPredicate(root, query, cb);
            if (buscaPredicate != null) {
                predicates.add(buscaPredicate);
            }

            Integer mesValor = parseInt(mes);
            Integer anoValor = parseInt(ano);
            Integer diaValor = parseInt(dia);

            if (anoValor != null && mesValor != null) {
                predicates.add(
                        cb.equal(
                                cb.function("year", Integer.class, root.get(ColunasPedido.CAMPO_DATA_CADASTRO)),
                                anoValor
                        )
                );
                predicates.add(
                        cb.equal(
                                cb.function("month", Integer.class, root.get(ColunasPedido.CAMPO_DATA_CADASTRO)),
                                mesValor
                        )
                );
                if (diaValor != null) {
                    predicates.add(
                            cb.equal(
                                    cb.function("day", Integer.class, root.get(ColunasPedido.CAMPO_DATA_CADASTRO)),
                                    diaValor
                            )
                    );
                }
            } else if (anoValor != null) {
                predicates.add(
                        cb.equal(
                                cb.function("year", Integer.class, root.get(ColunasPedido.CAMPO_DATA_CADASTRO)),
                                anoValor
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
