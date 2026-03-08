package com.example.demo.pedidos.localidade.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "`Bairro`")
public class Bairro {
    @EmbeddedId
    private LocalidadeId id;

    public LocalidadeId getId() {
        return id;
    }
}
