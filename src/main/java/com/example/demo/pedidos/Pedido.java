package com.example.demo.pedidos;

import jakarta.persistence.*;
import java.math.BigDecimal;

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
    private Integer cpf;

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
    private Integer telefone;

    @Column(name = ColunasPedido.TELEFONE_FIXO)
    private Integer telefoneFixo;

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

    @Column(name = ColunasPedido.CIDADE)
    private String cidade;

    @Column(name = ColunasPedido.CEP)
    private Integer cep;

    @Column(name = ColunasPedido.REFERENCIA)
    private String referencia;

    @Column(name = ColunasPedido.NUMERO_CLIENTE)
    private Integer numeroCliente;

    @Column(name = ColunasPedido.BAIRRO_CLIENTE)
    private String bairroCliente;

    @Column(name = ColunasPedido.CIDADE_CLIENTE)
    private String cidadeCliente;

    @Column(name = ColunasPedido.CEP_CLIENTE)
    private Integer cepCliente;

    @Column(name = ColunasPedido.REFERENCIA_CLIENTE)
    private String referenciaCliente;

    @Column(name = ColunasPedido.FLAG_OCULTO)
    private Boolean oculto;

    @Column(name = ColunasPedido.CLIENTE)
    private String cliente;

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

    public void setCpf(Integer cpf) {
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

    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }

    public void setTelefoneFixo(Integer telefoneFixo) {
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

    public void setCidade(String cidade) {
        this.cidade = cidade;
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

    public void setCidadeCliente(String cidadeCliente) {
        this.cidadeCliente = cidadeCliente;
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

    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public Integer getCpf() {
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

    public Integer getTelefone() {
        return telefone;
    }

    public Integer getTelefoneFixo() {
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

    public String getCidade() {
        return cidade;
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

    public String getCidadeCliente() {
        return cidadeCliente;
    }

    public Integer getCepCliente() {
        return cepCliente;
    }

    public String getReferenciaCliente() {
        return referenciaCliente;
    }

    public Boolean getFlagOculto() {
        return oculto;
    }

    public String getCliente() {
        return cliente;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
