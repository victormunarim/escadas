package com.example.demo.pedidos.repository;
import com.example.demo.pedidos.model.PedidoResumoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PedidoResumoRepository
        extends JpaRepository<PedidoResumoEntity, Long>,
        JpaSpecificationExecutor<PedidoResumoEntity> {
}