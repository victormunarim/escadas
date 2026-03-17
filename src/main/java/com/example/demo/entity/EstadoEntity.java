package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Estado")
public class EstadoEntity {
    @Id
    @Column(name = "Uf")
    private String uf;

    public String getUf() {
        return uf;
    }
}
