package com.example.demo.orcamentos.dto;

import com.example.demo.orcamentos.model.OrcamentoEntity;
import org.springframework.beans.BeanUtils;
import java.time.Instant;

public class OrcamentoDTO {
    private Long id;
    private String nome;
    private String bairro;
    private String descricao;
    private Instant dataCadastro;

    public OrcamentoDTO() {
    }

    public OrcamentoDTO(OrcamentoEntity entity) {
        BeanUtils.copyProperties(entity, this);
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

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDataCadastroFormatado() {
        if (dataCadastro == null) {
            return "-";
        }
        return com.example.demo.shared.util.FormatacaoUtil.formatarDataHora(
                java.time.LocalDateTime.ofInstant(dataCadastro, java.time.ZoneId.systemDefault())
        );
    }

    private Boolean flagEncerrado;

    public Boolean getFlagEncerrado() {
        return flagEncerrado;
    }

    public void setFlagEncerrado(Boolean flagEncerrado) {
        this.flagEncerrado = flagEncerrado;
    }
}
