package com.example.demo.tarefas.dto;

import com.example.demo.tarefas.model.TarefaEntity;
import com.example.demo.shared.util.FormatacaoUtil;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TarefaDTO {
    private Long id;
    private Long tipoTarefaId;
    private String tipoTarefaNome;
    private Long responsavelId;
    private String responsavelNome;
    private Boolean flagConcluida;
    private String extChave;
    private Long extId;
    private Instant dataCadastro;
    private String descricao;

    public TarefaDTO() {
    }

    public TarefaDTO(TarefaEntity entity) {
        this.id = entity.getId();
        if (entity.getTipoTarefa() != null) {
            this.tipoTarefaId = entity.getTipoTarefa().getId();
            this.tipoTarefaNome = entity.getTipoTarefa().getNome();
        }
        if (entity.getResponsavel() != null) {
            this.responsavelId = entity.getResponsavel().getId();
            this.responsavelNome = entity.getResponsavel().getLogin();
        }
        this.flagConcluida = entity.getFlagConcluida();
        this.extChave = entity.getExtChave();
        this.extId = entity.getExtId();
        this.dataCadastro = entity.getDataCadastro();
        this.descricao = entity.getDescricao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTipoTarefaId() {
        return tipoTarefaId;
    }

    public void setTipoTarefaId(Long tipoTarefaId) {
        this.tipoTarefaId = tipoTarefaId;
    }

    public String getTipoTarefaNome() {
        return tipoTarefaNome;
    }

    public void setTipoTarefaNome(String tipoTarefaNome) {
        this.tipoTarefaNome = tipoTarefaNome;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public void setResponsavelNome(String responsavelNome) {
        this.responsavelNome = responsavelNome;
    }

    public Boolean getFlagConcluida() {
        return flagConcluida;
    }

    public void setFlagConcluida(Boolean flagConcluida) {
        this.flagConcluida = flagConcluida;
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
        return FormatacaoUtil.formatarDataHora(
                LocalDateTime.ofInstant(dataCadastro, ZoneId.systemDefault())
        );
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
