package com.example.demo.orcamentos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "etiquetas", schema = "escadas")
public class EtiquetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    public EtiquetaEntity() {
    }

    public EtiquetaEntity(String nome) {
        this.nome = nome;
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
}
