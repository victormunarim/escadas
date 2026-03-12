package com.example.demo.pedidos.arquivo.repository;

import com.example.demo.pedidos.arquivo.model.ArquivoPedido;
import com.example.demo.pedidos.arquivo.model.ArquivoPedidoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioArquivoPedido extends JpaRepository<ArquivoPedido, ArquivoPedidoId> {

    List<ArquivoPedido> findByIdPedidoIdOrderByIdNomeAsc(Long pedidoId);
}
