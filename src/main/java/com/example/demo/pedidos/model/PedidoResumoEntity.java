package com.example.demo.pedidos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pedidos", schema = "escadas")
public class PedidoResumoEntity {

    @Id
    @Column(name = "id_pedido", nullable = false)
    private Integer id;

    @Column(name = "numero_pedido")
    private Integer numeroPedido;

    @Column(name = "cliente_nome")
    private String nomeCliente;

    @Column(name = "email")
    private String email;

    @Column(name = "cpf")
    private Long cpf;

    @Column(name = "rg")
    private Integer rg;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "servico_social")
    private String servicoSocial;

    @Column(name = "profissao")
    private String profissao;

    @Column(name = "adm_obra")
    private String admObra;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "telefone_fixo")
    private String telefoneFixo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "acabamento")
    private String acabamento;

    @Column(name = "tubos")
    private String tubos;

    @Column(name = "revestimento")
    private Boolean revestimento;

    @Column(name = "valor_total", precision = 38, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "prazo_montagem")
    private Integer prazoMontagem;

    @Column(name = "data_cadastro")
    private Instant dataCadastro;

    @Column(name = "flag_oculto")
    private Boolean flagOculto;

    @Column(name = "valor", precision = 38, scale = 2)
    private BigDecimal valor;

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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}