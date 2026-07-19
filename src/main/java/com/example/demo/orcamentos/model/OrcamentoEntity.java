package com.example.demo.orcamentos.model;

import com.example.demo.pedidos.model.PedidoEntity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "orcamentos", schema = "escadas")
public class OrcamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "descricao", length = 2000)
    private String descricao;

    @Column(name = "flag_oculto", nullable = false)
    private Boolean flagOculto = false;

    @Column(name = "data_cadastro")
    private Instant dataCadastro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "etiquetas_id")
    private EtiquetaEntity etiqueta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    private PedidoEntity pedido;

    @Column(name = "flag_encerrado", nullable = false)
    private Boolean flagEncerrado = false;

    public OrcamentoEntity() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.dataCadastro == null) {
            this.dataCadastro = Instant.now();
        }
    }

    public boolean ehTecnico() {
        return this.pedido != null;
    }

    public void atualizarEtiqueta(EtiquetaEntity novaEtiqueta) {
        if (novaEtiqueta == null) {
            throw new IllegalArgumentException("A etiqueta é obrigatória.");
        }
        if (ehTecnico()) {
            if (this.etiqueta != null && !novaEtiqueta.getId().equals(this.etiqueta.getId())) {
                throw new IllegalArgumentException("Não é possível alterar a etiqueta de um Técnico.");
            }
        } else {
            this.etiqueta = novaEtiqueta;
        }
    }

    public void vincularPedido(PedidoEntity pedido) {
        if (pedido == null) {
            if (!ehTecnico()) {
                this.pedido = null;
            }
            return;
        }
        if (this.etiqueta == null || !"proposta".equalsIgnoreCase(this.etiqueta.getNome())) {
            throw new IllegalArgumentException(
                    "Não é possível associar um pedido a um orçamento que não tenha a etiqueta 'Proposta'."
            );
        }
        this.pedido = pedido;
        this.flagEncerrado = true;
    }

    public void encerrar() {
        this.flagEncerrado = true;
    }

    public void reabrir() {
        this.flagEncerrado = false;
        this.pedido = null;
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

    public Boolean getFlagOculto() {
        return flagOculto;
    }

    public void setFlagOculto(Boolean flagOculto) {
        this.flagOculto = flagOculto;
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getFlagEncerrado() {
        return flagEncerrado;
    }

    public void setFlagEncerrado(Boolean flagEncerrado) {
        this.flagEncerrado = flagEncerrado;
    }

    public EtiquetaEntity getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(EtiquetaEntity etiqueta) {
        this.etiqueta = etiqueta;
    }

    public PedidoEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }
}
