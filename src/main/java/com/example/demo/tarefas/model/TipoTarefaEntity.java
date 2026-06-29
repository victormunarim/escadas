package com.example.demo.tarefas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_tarefa", schema = "escadas")
public class TipoTarefaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    public TipoTarefaEntity() {
    }

    public TipoTarefaEntity(String nome) {
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
