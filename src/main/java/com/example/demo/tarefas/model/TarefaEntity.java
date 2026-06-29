package com.example.demo.tarefas.model;

import com.example.demo.auth.model.UsuarioEntity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tarefas", schema = "escadas")
public class TarefaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_tarefa", nullable = false)
    private TipoTarefaEntity tipoTarefa;

    @Column(name = "data_cadastro")
    private Instant dataCadastro;

    @Column(name = "flag_oculto", nullable = false)
    private Boolean flagOculto = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsavel", nullable = false)
    private UsuarioEntity responsavel;

    @Column(name = "flag_concluida", nullable = false)
    private Boolean flagConcluida = false;

    @Column(name = "ext_chave")
    private String extChave;

    @Column(name = "ext_id")
    private Long extId;

    @Column(name = "descricao", length = 1000)
    private String descricao;

    public TarefaEntity() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.dataCadastro == null) {
            this.dataCadastro = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoTarefaEntity getTipoTarefa() {
        return tipoTarefa;
    }

    public void setTipoTarefa(TipoTarefaEntity tipoTarefa) {
        this.tipoTarefa = tipoTarefa;
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getFlagOculto() {
        return flagOculto;
    }

    public void setFlagOculto(Boolean flagOculto) {
        this.flagOculto = flagOculto;
    }

    public UsuarioEntity getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UsuarioEntity responsavel) {
        this.responsavel = responsavel;
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
