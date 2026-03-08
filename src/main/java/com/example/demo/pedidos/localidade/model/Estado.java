package com.example.demo.pedidos.localidade.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "`Estado`")
public class Estado {
    @Id
    @Column(name = "`Uf`")
    private String uf;

    public String getUf() {
        return uf;
    }
}
