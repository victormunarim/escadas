package com.example.demo.pedidos.arquivo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Table;

@Entity
@Table(name = "arquivos")
public class ArquivoPedido {

    @EmbeddedId
    private ArquivoPedidoId id;

    protected ArquivoPedido() {
    }

    public ArquivoPedido(String nome, Long pedidoId, String link) {
        this.id = new ArquivoPedidoId(nome, pedidoId, link);
    }

    public String getNome() {
        return id == null ? null : id.getNome();
    }

    public Long getPedidoId() {
        return id == null ? null : id.getPedidoId();
    }

    public String getLink() {
        return id == null ? null : id.getLink();
    }
}
