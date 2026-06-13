package com.example.demo.pedidos.repository;
import com.example.demo.pedidos.model.ArquivoPedidoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArquivoPedidoRepository extends JpaRepository<ArquivoPedidoEntity, Long> {

    List<ArquivoPedidoEntity> findByPedidoIdOrderByNomeAsc(Long pedidoId);
}