package com.example.demo.pedidos.repository;

import com.example.demo.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositorioPedido
        extends JpaRepository<Pedido, Long>,
        JpaSpecificationExecutor<Pedido> {
}
