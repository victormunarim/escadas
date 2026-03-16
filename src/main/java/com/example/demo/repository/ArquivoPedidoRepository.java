package com.example.demo.repository;

import com.example.demo.entity.ArquivoPedidoEntity;
import com.example.demo.entity.ArquivoPedidoIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArquivoPedidoRepository extends JpaRepository<ArquivoPedidoEntity, ArquivoPedidoIdEntity> {

    List<ArquivoPedidoEntity> findByIdPedidoIdOrderByIdNomeAsc(Long pedidoId);
}
