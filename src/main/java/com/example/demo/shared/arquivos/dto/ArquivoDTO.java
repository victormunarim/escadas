package com.example.demo.shared.arquivos.dto;

import com.example.demo.shared.arquivos.model.ArquivoEntity;
import org.springframework.beans.BeanUtils;

public class ArquivoDTO {
    private Long id;
    private String nome;
    private String extChave;
    private Long extId;
    private String link;
    private Integer etapa;

    public ArquivoDTO() {
    }

    public ArquivoDTO(ArquivoEntity arquivo) {
        BeanUtils.copyProperties(arquivo, this);
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
