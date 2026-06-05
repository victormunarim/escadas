package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "enderecos", schema = "escadas")
public class EnderecosEntity {
    @Id
    @Column(name = "pedido_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;

    @Column(name = "estado_id")
    private Integer estadoId;

    @Column(name = "estado_cliente_id")
    private Integer estadoClienteId;

    @Column(name = "municipio_id")
    private Integer municipioId;

    @Column(name = "municipio_cliente_id")
    private Integer municipioClienteId;

    @Column(name = "bairro_id")
    private Integer bairroId;

    @Column(name = "bairro_cliente_id")
    private Integer bairroClienteId;

    @Column(name = "cep")
    private Integer cep;

    @Column(name = "cep_cliente_id")
    private Integer cepClienteId;

    @Column(name = "referencia")
    private String referencia;

    @Column(name = "referencia_cliente")
    private String referenciaCliente;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PedidoEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }

    public Integer getEstadoClienteId() {
        return estadoClienteId;
    }

    public void setEstadoClienteId(Integer estadoClienteId) {
        this.estadoClienteId = estadoClienteId;
    }

    public Integer getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(Integer municipioId) {
        this.municipioId = municipioId;
    }

    public Integer getMunicipioClienteId() {
        return municipioClienteId;
    }

    public void setMunicipioClienteId(Integer municipioClienteId) {
        this.municipioClienteId = municipioClienteId;
    }

    public Integer getBairroId() {
        return bairroId;
    }

    public void setBairroId(Integer bairroId) {
        this.bairroId = bairroId;
    }

    public Integer getBairroClienteId() {
        return bairroClienteId;
    }

    public void setBairroClienteId(Integer bairroClienteId) {
        this.bairroClienteId = bairroClienteId;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public Integer getCepClienteId() {
        return cepClienteId;
    }

    public void setCepClienteId(Integer cepClienteId) {
        this.cepClienteId = cepClienteId;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferenciaCliente() {
        return referenciaCliente;
    }

    public void setReferenciaCliente(String referenciaCliente) {
        this.referenciaCliente = referenciaCliente;
    }

}
