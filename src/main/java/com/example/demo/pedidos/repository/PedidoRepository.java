package com.example.demo.pedidos.repository;
import com.example.demo.pedidos.model.PedidoEntity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository
        extends JpaRepository<PedidoEntity, Long>,
        JpaSpecificationExecutor<PedidoEntity> {
}