package com.example.demo.repository;

import com.example.demo.entity.ArquivoPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArquivoPedidoRepository extends JpaRepository<ArquivoPedidoEntity, Long> {

    List<ArquivoPedidoEntity> findByPedidoIdOrderByNomeAsc(Long pedidoId);
}
