package com.example.demo.pedidos.localidade.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LocalidadeId implements Serializable {
    @Column(name = "`Nome`")
    private String nome;

    @Column(name = "`Uf`")
    private String uf;

    public String getNome() {
        return nome;
    }

    public String getUf() {
        return uf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalidadeId that)) {
            return false;
        }
        return Objects.equals(nome, that.nome) && Objects.equals(uf, that.uf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, uf);
    }
}
