package com.example.demo.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "`Bairro`")
public class BairroEntity {
    @EmbeddedId
    private LocalidadeIdEntity id;

    public LocalidadeIdEntity getId() {
        return id;
    }
}
