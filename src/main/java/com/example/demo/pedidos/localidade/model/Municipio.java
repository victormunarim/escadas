package com.example.demo.pedidos.localidade.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "`Municipio`")
public class Municipio {
    @EmbeddedId
    private LocalidadeId id;

    public LocalidadeId getId() {
        return id;
    }
}
