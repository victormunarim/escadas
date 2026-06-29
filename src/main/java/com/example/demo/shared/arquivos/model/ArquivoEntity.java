package com.example.demo.shared.arquivos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "arquivos", schema = "escadas")
public class ArquivoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "ext_chave", nullable = false)
    private String extChave;

    @Column(name = "ext_id", nullable = false)
    private Long extId;

    @Column(name = "link", nullable = false, length = 1000)
    private String link;

    @Column(name = "etapa")
    private Integer etapa;

    public ArquivoEntity() {
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

    public String getExtChave() {
        return extChave;
    }

    public void setExtChave(String extChave) {
        this.extChave = extChave;
    }

    public Long getExtId() {
        return extId;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getEtapa() {
        return etapa;
    }

    public void setEtapa(Integer etapa) {
        this.etapa = etapa;
    }
}
