package com.example.demo.dto;

import com.example.demo.entity.PedidoEntity;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoDTO {
    private Integer id;
    private Integer numeroPedido;
    private String nomeCliente;
    private String email;
    private Long cpf;
    private Integer rg;
    private String cnpj;
    private String servicoSocial;
    private String profissao;
    private String admObra;
    private String telefone;
    private String telefoneFixo;
    private String descricao;
    private String acabamento;
    private String tubos;
    private Boolean revestimento;
    private BigDecimal valorTotal;
    private Integer prazoMontagem;
    private Integer numero;
    private Integer estadoId;
    private Integer municipioId;
    private Integer bairroId;
    private String bairro;
    private String municipio;
    private Integer cep;
    private String referencia;
    private Integer numeroCliente;
    private Integer estadoClienteId;
    private Integer municipioClienteId;
    private Integer bairroClienteId;
    private String bairroCliente;
    private String municipioCliente;
    private Integer cepCliente;
    private String referenciaCliente;
    private LocalDateTime dataCadastro;
    private Boolean flagOculto;
    private BigDecimal valor;

    public PedidoDTO(PedidoEntity pedidoEntity) {
        BeanUtils.copyProperties(pedidoEntity, this);
    }

    public PedidoDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public Integer getRg() {
        return rg;
    }

    public void setRg(Integer rg) {
        this.rg = rg;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getServicoSocial() {
        return servicoSocial;
    }

    public void setServicoSocial(String servicoSocial) {
        this.servicoSocial = servicoSocial;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getAdmObra() {
        return admObra;
    }

    public void setAdmObra(String admObra) {
        this.admObra = admObra;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAcabamento() {
        return acabamento;
    }

    public void setAcabamento(String acabamento) {
        this.acabamento = acabamento;
    }

    public String getTubos() {
        return tubos;
    }

    public void setTubos(String tubos) {
        this.tubos = tubos;
    }

    public Boolean getRevestimento() {
        return revestimento;
    }

    public void setRevestimento(Boolean revestimento) {
        this.revestimento = revestimento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getPrazoMontagem() {
        return prazoMontagem;
    }

    public void setPrazoMontagem(Integer prazoMontagem) {
        this.prazoMontagem = prazoMontagem;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }

    public Integer getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(Integer municipioId) {
        this.municipioId = municipioId;
    }

    public Integer getBairroId() {
        return bairroId;
    }

    public void setBairroId(Integer bairroId) {
        this.bairroId = bairroId;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(Integer numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public Integer getEstadoClienteId() {
        return estadoClienteId;
    }

    public void setEstadoClienteId(Integer estadoClienteId) {
        this.estadoClienteId = estadoClienteId;
    }

    public Integer getMunicipioClienteId() {
        return municipioClienteId;
    }

    public void setMunicipioClienteId(Integer municipioClienteId) {
        this.municipioClienteId = municipioClienteId;
    }

    public Integer getBairroClienteId() {
        return bairroClienteId;
    }

    public void setBairroClienteId(Integer bairroClienteId) {
        this.bairroClienteId = bairroClienteId;
    }

    public String getBairroCliente() {
        return bairroCliente;
    }

    public void setBairroCliente(String bairroCliente) {
        this.bairroCliente = bairroCliente;
    }

    public String getMunicipioCliente() {
        return municipioCliente;
    }

    public void setMunicipioCliente(String municipioCliente) {
        this.municipioCliente = municipioCliente;
    }

    public Integer getCepCliente() {
        return cepCliente;
    }

    public void setCepCliente(Integer cepCliente) {
        this.cepCliente = cepCliente;
    }

    public String getReferenciaCliente() {
        return referenciaCliente;
    }

    public void setReferenciaCliente(String referenciaCliente) {
        this.referenciaCliente = referenciaCliente;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getFlagOculto() {
        return flagOculto;
    }

    public void setFlagOculto(Boolean flagOculto) {
        this.flagOculto = flagOculto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
