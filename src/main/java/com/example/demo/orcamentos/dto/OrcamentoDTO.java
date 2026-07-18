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

    private Long etiquetaId;
    private String etiquetaNome;

    private Integer pedidoId;
    private Integer pedidoNumero;
    private String pedidoCliente;
    private Boolean flagTecnico;

    public OrcamentoDTO() {
    }

    public OrcamentoDTO(OrcamentoEntity entity) {
        BeanUtils.copyProperties(entity, this);
        if (entity.getEtiqueta() != null) {
            this.etiquetaId = entity.getEtiqueta().getId();
            this.etiquetaNome = entity.getEtiqueta().getNome();
        }
        if (entity.getPedido() != null) {
            this.pedidoId = entity.getPedido().getId();
            this.pedidoNumero = entity.getPedido().getNumeroPedido();
            this.pedidoCliente = entity.getPedido().getNomeCliente();
        }
        this.flagTecnico = entity.getPedido() != null;
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

    public Long getEtiquetaId() {
        return etiquetaId;
    }

    public void setEtiquetaId(Long etiquetaId) {
        this.etiquetaId = etiquetaId;
    }

    public String getEtiquetaNome() {
        return etiquetaNome;
    }

    public void setEtiquetaNome(String etiquetaNome) {
        this.etiquetaNome = etiquetaNome;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getPedidoNumero() {
        return pedidoNumero;
    }

    public void setPedidoNumero(Integer pedidoNumero) {
        this.pedidoNumero = pedidoNumero;
    }

    public String getPedidoCliente() {
        return pedidoCliente;
    }

    public void setPedidoCliente(String pedidoCliente) {
        this.pedidoCliente = pedidoCliente;
    }

    public Boolean getFlagTecnico() {
        return flagTecnico;
    }

    public void setFlagTecnico(Boolean flagTecnico) {
        this.flagTecnico = flagTecnico;
    }
}
