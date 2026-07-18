package com.example.demo.pedidos.model;

import com.example.demo.localidades.model.BairroEntity;
import com.example.demo.localidades.model.EstadoEntity;
import com.example.demo.localidades.model.MunicipioEntity;
import com.example.demo.pedidos.config.ColunasPedido;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private EstadoEntity estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_cliente_id")
    private EstadoEntity estadoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id")
    private MunicipioEntity municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_cliente_id")
    private MunicipioEntity municipioCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bairro_id")
    private BairroEntity bairro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bairro_cliente_id")
    private BairroEntity bairroCliente;

    @Column(name = ColunasPedido.CEP)
    private Integer cep;

    @Column(name = "cep_cliente_id")
    private Integer cepClienteId;

    @Column(name = ColunasPedido.REFERENCIA)
    private String referencia;

    @Column(name = ColunasPedido.REFERENCIA_CLIENTE)
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
        return estado == null ? null : estado.getId();
    }

    public void setEstadoId(Integer estadoId) {
        if (estadoId == null) {
            this.estado = null;
        } else {
            this.estado = new EstadoEntity();
            this.estado.setId(estadoId);
        }
    }

    public Integer getEstadoClienteId() {
        return estadoCliente == null ? null : estadoCliente.getId();
    }

    public void setEstadoClienteId(Integer estadoClienteId) {
        if (estadoClienteId == null) {
            this.estadoCliente = null;
        } else {
            this.estadoCliente = new EstadoEntity();
            this.estadoCliente.setId(estadoClienteId);
        }
    }

    public Integer getMunicipioId() {
        return municipio == null ? null : municipio.getId();
    }

    public void setMunicipioId(Integer municipioId) {
        if (municipioId == null) {
            this.municipio = null;
        } else {
            this.municipio = new MunicipioEntity();
            this.municipio.setId(municipioId);
        }
    }

    public Integer getMunicipioClienteId() {
        return municipioCliente == null ? null : municipioCliente.getId();
    }

    public void setMunicipioClienteId(Integer municipioClienteId) {
        if (municipioClienteId == null) {
            this.municipioCliente = null;
        } else {
            this.municipioCliente = new MunicipioEntity();
            this.municipioCliente.setId(municipioClienteId);
        }
    }

    public Integer getBairroId() {
        return bairro == null ? null : bairro.getId();
    }

    public void setBairroId(Integer bairroId) {
        if (bairroId == null) {
            this.bairro = null;
        } else {
            this.bairro = new BairroEntity();
            this.bairro.setId(bairroId);
        }
    }

    public Integer getBairroClienteId() {
        return bairroCliente == null ? null : bairroCliente.getId();
    }

    public void setBairroClienteId(Integer bairroClienteId) {
        if (bairroClienteId == null) {
            this.bairroCliente = null;
        } else {
            this.bairroCliente = new BairroEntity();
            this.bairroCliente.setId(bairroClienteId);
        }
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

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }

    public EstadoEntity getEstadoCliente() {
        return estadoCliente;
    }

    public void setEstadoCliente(EstadoEntity estadoCliente) {
        this.estadoCliente = estadoCliente;
    }

    public MunicipioEntity getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioEntity municipio) {
        this.municipio = municipio;
    }

    public MunicipioEntity getMunicipioCliente() {
        return municipioCliente;
    }

    public void setMunicipioCliente(MunicipioEntity municipioCliente) {
        this.municipioCliente = municipioCliente;
    }

    public BairroEntity getBairro() {
        return bairro;
    }

    public void setBairro(BairroEntity bairro) {
        this.bairro = bairro;
    }

    public BairroEntity getBairroCliente() {
        return bairroCliente;
    }

    public void setBairroCliente(BairroEntity bairroCliente) {
        this.bairroCliente = bairroCliente;
    }
}