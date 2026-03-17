package com.example.demo.dto;

import com.example.demo.entity.ArquivoPedidoEntity;
import org.springframework.beans.BeanUtils;

public class ArquivoPedidoDTO {
    private Long id;
    private String nome;
    private Long pedidoId;
    private String link;

    public ArquivoPedidoDTO() {
    }

    public ArquivoPedidoDTO(ArquivoPedidoEntity arquivoPedido) {
        BeanUtils.copyProperties(arquivoPedido, this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
