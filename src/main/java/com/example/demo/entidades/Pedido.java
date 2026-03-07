package com.example.demo.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = PedidoCols.TABELA)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = PedidoCols.ID_PEDIDO)
    private Long id;

    @Column(name = PedidoCols.NUMERO_PEDIDO)
    private Integer numeroPedido;

    @Column(name = PedidoCols.CLIENTE_NOME)
    private String clienteNome;

    @Column(name = PedidoCols.EMAIL)
    private String email;

    @Column(name = PedidoCols.CPF)
    private Integer cpf;

    @Column(name = PedidoCols.RG)
    private Integer rg;

    @Column(name = PedidoCols.CNPJ)
    private String cnpj;

    @Column(name = PedidoCols.SERVICO_SOCIAL)
    private String servicoSocial;

    @Column(name = PedidoCols.PROFISSAO)
    private String profissao;

    @Column(name = PedidoCols.ADM_OBRA)
    private String admObra;

    @Column(name = PedidoCols.TELEFONE)
    private Integer telefone;

    @Column(name = PedidoCols.TELEFONE_FIXO)
    private Integer telefoneFixo;

    @Column(name = PedidoCols.DESCRICAO, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = PedidoCols.ACABAMENTO)
    private String acabamento;

    @Column(name = PedidoCols.TUBOS)
    private String tubos;

    @Column(name = PedidoCols.REVESTIMENTO)
    private Boolean revestimento;

    @Column(name = PedidoCols.VALOR_TOTAL)
    private BigDecimal valorTotal;

    @Column(name = PedidoCols.PRAZO_MONTAGEM)
    private Integer prazoMontagem;

    @Column(name = PedidoCols.NUMERO)
    private Integer numero;

    @Column(name = PedidoCols.BAIRRO)
    private String bairro;

    @Column(name = PedidoCols.CIDADE)
    private String cidade;

    @Column(name = PedidoCols.CEP)
    private Integer cep;

    @Column(name = PedidoCols.REFERENCIA)
    private String referencia;

    @Column(name = PedidoCols.NUMERO_CLIENTE)
    private Integer numeroCliente;

    @Column(name = PedidoCols.BAIRRO_CLIENTE)
    private String bairroCliente;

    @Column(name = PedidoCols.CIDADE_CLIENTE)
    private String cidadeCliente;

    @Column(name = PedidoCols.CEP_CLIENTE)
    private Integer cepCliente;

    @Column(name = PedidoCols.REFERENCIA_CLIENTE)
    private String referenciaCliente;

    @Column(name = PedidoCols.FLAG_OCULTO)
    private Boolean flagOculto;

    @Column(name = PedidoCols.CLIENTE)
    private String cliente;

    @Column(name = PedidoCols.VALOR)
    private BigDecimal valor;

    public Long getId() {
        return id;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public String getClienteNome() {
        return clienteNome;
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
        return flagOculto;
    }

    public String getCliente() {
        return cliente;
    }

    public BigDecimal getValor() {
        return valor;
    }
}