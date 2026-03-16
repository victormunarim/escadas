package com.example.demo.repository;

import com.example.demo.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PedidoRepository
        extends JpaRepository<PedidoEntity, Long>,
        JpaSpecificationExecutor<PedidoEntity>,
        PedidoRepositoryCustom {
}
