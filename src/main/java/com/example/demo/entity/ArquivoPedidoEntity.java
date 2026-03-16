package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Table;

@Entity
@Table(name = "arquivos")
public class ArquivoPedidoEntity {

    @EmbeddedId
    private ArquivoPedidoIdEntity id;

    protected ArquivoPedidoEntity() {
    }

    public ArquivoPedidoEntity(String nome, Long pedidoId, String link) {
        this.id = new ArquivoPedidoIdEntity(nome, pedidoId, link);
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
