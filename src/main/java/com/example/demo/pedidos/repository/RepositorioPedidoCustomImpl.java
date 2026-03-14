package com.example.demo.pedidos.repository;

import com.example.demo.pedidos.dto.PedidoResumo;
import com.example.demo.pedidos.model.ColunasPedido;
import com.example.demo.pedidos.model.Pedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RepositorioPedidoCustomImpl implements RepositorioPedidoCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PedidoResumo> buscarResumo(Specification<Pedido> spec, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PedidoResumo> cq = cb.createQuery(PedidoResumo.class);
        Root<Pedido> root = cq.from(Pedido.class);

        cq.select(cb.construct(
                PedidoResumo.class,
                root.get(ColunasPedido.CAMPO_ID_PEDIDO),
                root.get(ColunasPedido.CAMPO_NUMERO_PEDIDO),
                root.get(ColunasPedido.CAMPO_CLIENTE_NOME),
                root.get(ColunasPedido.CAMPO_EMAIL),
                root.get(ColunasPedido.CAMPO_CPF),
                root.get(ColunasPedido.CAMPO_RG),
                root.get(ColunasPedido.CAMPO_CNPJ),
                root.get(ColunasPedido.CAMPO_SERVICO_SOCIAL),
                root.get(ColunasPedido.CAMPO_PROFISSAO),
                root.get(ColunasPedido.CAMPO_ADM_OBRA),
                root.get(ColunasPedido.CAMPO_TELEFONE),
                root.get(ColunasPedido.CAMPO_TELEFONE_FIXO),
                root.get(ColunasPedido.CAMPO_DESCRICAO),
                root.get(ColunasPedido.CAMPO_ACABAMENTO),
                root.get(ColunasPedido.CAMPO_TUBOS),
                root.get(ColunasPedido.CAMPO_REVESTIMENTO),
                root.get(ColunasPedido.CAMPO_VALOR_TOTAL),
                root.get(ColunasPedido.CAMPO_PRAZO_MONTAGEM),
                root.get(ColunasPedido.CAMPO_NUMERO),
                root.get(ColunasPedido.CAMPO_BAIRRO),
                root.get(ColunasPedido.CAMPO_MUNICIPIO),
                root.get(ColunasPedido.CAMPO_CEP),
                root.get(ColunasPedido.CAMPO_REFERENCIA),
                root.get(ColunasPedido.CAMPO_NUMERO_CLIENTE),
                root.get(ColunasPedido.CAMPO_BAIRRO_CLIENTE),
                root.get(ColunasPedido.CAMPO_MUNICIPIO_CLIENTE),
                root.get(ColunasPedido.CAMPO_CEP_CLIENTE),
                root.get(ColunasPedido.CAMPO_REFERENCIA_CLIENTE),
                root.get(ColunasPedido.CAMPO_DATA_CADASTRO),
                root.get(ColunasPedido.CAMPO_FLAG_OCULTO),
                root.get(ColunasPedido.CAMPO_VALOR)
        ));

        if (spec != null) {
            Predicate where = spec.toPredicate(root, cq, cb);
            if (where != null) {
                cq.where(where);
            }
        }

        TypedQuery<PedidoResumo> query = entityManager.createQuery(cq);
        if (pageable != null) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return query.getResultList();
    }
}
