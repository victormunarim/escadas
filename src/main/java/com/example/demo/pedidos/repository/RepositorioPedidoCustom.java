package com.example.demo.pedidos.repository;

import com.example.demo.pedidos.dto.PedidoResumo;
import com.example.demo.pedidos.model.Pedido;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface RepositorioPedidoCustom {
    List<PedidoResumo> buscarResumo(Specification<Pedido> spec, Pageable pageable);
}
