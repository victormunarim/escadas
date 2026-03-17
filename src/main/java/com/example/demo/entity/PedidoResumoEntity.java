package com.example.demo.entity;

import com.example.demo.constants.ColunasPedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = ColunasPedido.TABELA)
public class PedidoResumoEntity {

    @Id
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

    @Column(name = ColunasPedido.DATA_CADASTRO)
    private LocalDateTime dataCadastro;

    @Column(name = ColunasPedido.FLAG_OCULTO)
    private Boolean flagOculto;

    @Column(name = ColunasPedido.VALOR)
    private BigDecimal valor;

    public PedidoResumoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
