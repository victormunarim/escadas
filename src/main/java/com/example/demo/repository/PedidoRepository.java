package com.example.demo.repository;

import com.example.demo.entity.PedidoEntity;
import com.example.demo.entity.PedidoResumoEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository
        extends JpaRepository<PedidoEntity, Long>,
        JpaSpecificationExecutor<PedidoEntity> {
}
