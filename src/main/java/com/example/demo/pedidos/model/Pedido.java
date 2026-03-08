package com.example.demo.pedidos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = ColunasPedido.TABELA)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ColunasPedido.ID_PEDIDO)
    private Long id;

    @Column(name = ColunasPedido.NUMERO_PEDIDO)
    private Integer numeroPedido;

    @Column(name = ColunasPedido.CLIENTE_NOME)
    private String nomeCliente;

    @Column(name = ColunasPedido.EMAIL)
    private String email;

    @Column(name = ColunasPedido.CPF)
    private Long cpf;

    @Column(name = ColunasPedido.RG)
    private Integer rg;

    @Column(name = ColunasPedido.CNPJ)
    private String cnpj;

    @Column(name = ColunasPedido.SERVICO_SOCIAL)
    private String servicoSocial;

    @Column(name = ColunasPedido.PROFISSAO)
    private String profissao;

    @Column(name = ColunasPedido.ADM_OBRA)
    private String admObra;

    @Column(name = ColunasPedido.TELEFONE)
    private String telefone;

    @Column(name = ColunasPedido.TELEFONE_FIXO)
    private String telefoneFixo;

    @Column(name = ColunasPedido.DESCRICAO, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = ColunasPedido.ACABAMENTO)
    private String acabamento;

    @Column(name = ColunasPedido.TUBOS)
    private String tubos;

    @Column(name = ColunasPedido.REVESTIMENTO)
    private Boolean revestimento;

    @Column(name = ColunasPedido.VALOR_TOTAL)
    private BigDecimal valorTotal;

    @Column(name = ColunasPedido.PRAZO_MONTAGEM)
    private Integer prazoMontagem;

    @Column(name = ColunasPedido.NUMERO)
    private Integer numero;

    @Column(name = ColunasPedido.BAIRRO)
    private String bairro;

    @Column(name = ColunasPedido.MUNICIPIO)
    private String municipio;

    @Column(name = ColunasPedido.CEP)
    private Integer cep;

    @Column(name = ColunasPedido.REFERENCIA)
    private String referencia;

    @Column(name = ColunasPedido.NUMERO_CLIENTE)
    private Integer numeroCliente;

    @Column(name = ColunasPedido.BAIRRO_CLIENTE)
    private String bairroCliente;

    @Column(name = ColunasPedido.MUNICIPIO_CLIENTE)
    private String municipioCliente;

    @Column(name = ColunasPedido.CEP_CLIENTE)
    private Integer cepCliente;

    @Column(name = ColunasPedido.REFERENCIA_CLIENTE)
    private String referenciaCliente;

    @Column(name = ColunasPedido.DATA_CADASTRO)
    private LocalDateTime dataCadastro;

    @Column(name = ColunasPedido.FLAG_OCULTO)
    private Boolean oculto;

    @Column(name = ColunasPedido.VALOR)
    private BigDecimal valor;

    public Long getId() {
        return id;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public void setRg(Integer rg) {
        this.rg = rg;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setServicoSocial(String servicoSocial) {
        this.servicoSocial = servicoSocial;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public void setAdmObra(String admObra) {
        this.admObra = admObra;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setAcabamento(String acabamento) {
        this.acabamento = acabamento;
    }

    public void setTubos(String tubos) {
        this.tubos = tubos;
    }

    public void setRevestimento(Boolean revestimento) {
        this.revestimento = revestimento;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setPrazoMontagem(Integer prazoMontagem) {
        this.prazoMontagem = prazoMontagem;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void setNumeroCliente(Integer numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public void setBairroCliente(String bairroCliente) {
        this.bairroCliente = bairroCliente;
    }

    public void setMunicipioCliente(String municipioCliente) {
        this.municipioCliente = municipioCliente;
    }

    public void setCepCliente(Integer cepCliente) {
        this.cepCliente = cepCliente;
    }

    public void setReferenciaCliente(String referenciaCliente) {
        this.referenciaCliente = referenciaCliente;
    }

    public void setFlagOculto(Boolean oculto) {
        this.oculto = oculto;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getEmail() {
        return email;
    }

    public Long getCpf() {
        return cpf;
    }

    public Integer getRg() {
        return rg;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getServicoSocial() {
        return servicoSocial;
    }

    public String getProfissao() {
        return profissao;
    }

    public String getAdmObra() {
        return admObra;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getAcabamento() {
        return acabamento;
    }

    public String getTubos() {
        return tubos;
    }

    public Boolean getRevestimento() {
        return revestimento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public Integer getPrazoMontagem() {
        return prazoMontagem;
    }

    public Integer getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public Integer getCep() {
        return cep;
    }

    public String getReferencia() {
        return referencia;
    }

    public Integer getNumeroCliente() {
        return numeroCliente;
    }

    public String getBairroCliente() {
        return bairroCliente;
    }

    public String getMunicipioCliente() {
        return municipioCliente;
    }

    public Integer getCepCliente() {
        return cepCliente;
    }

    public String getReferenciaCliente() {
        return referenciaCliente;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public Boolean getFlagOculto() {
        return oculto;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
