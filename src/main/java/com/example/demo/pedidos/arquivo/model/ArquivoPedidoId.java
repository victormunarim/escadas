package com.example.demo.pedidos.arquivo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ArquivoPedidoId implements Serializable {

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "link", nullable = false, length = 1000)
    private String link;

    protected ArquivoPedidoId() {
    }

    public ArquivoPedidoId(String nome, Long pedidoId, String link) {
        this.nome = nome;
        this.pedidoId = pedidoId;
        this.link = link;
    }

    public String getNome() {
        return nome;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArquivoPedidoId that)) {
            return false;
        }
        return Objects.equals(nome, that.nome)
                && Objects.equals(pedidoId, that.pedidoId)
                && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, pedidoId, link);
    }
}
