package com.example.demo.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositorioPedido
        extends JpaRepository<Pedido, Long>,
        JpaSpecificationExecutor<Pedido> {
}