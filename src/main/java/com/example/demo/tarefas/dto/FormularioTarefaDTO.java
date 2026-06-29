package com.example.demo.tarefas.dto;

public class FormularioTarefaDTO {
    private Long tipoTarefaId;
    private Long responsavelId;
    private Boolean flagConcluida;
    private String extChave;
    private Long extId;
    private String descricao;

    public FormularioTarefaDTO() {
    }

    public Long getTipoTarefaId() {
        return tipoTarefaId;
    }

    public void setTipoTarefaId(Long tipoTarefaId) {
        this.tipoTarefaId = tipoTarefaId;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
