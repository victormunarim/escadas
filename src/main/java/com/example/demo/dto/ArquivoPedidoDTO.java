package com.example.demo.dto;

public class ArquivoPedidoDTO {
    private final String nome;
    private final Long pedidoId;
    private final String link;

    public ArquivoPedidoDTO(String nome, Long pedidoId, String link) {
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
}
