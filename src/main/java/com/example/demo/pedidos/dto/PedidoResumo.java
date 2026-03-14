package com.example.demo.pedidos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoResumo {
    private final Long id;
    private final Integer numeroPedido;
    private final String nomeCliente;
    private final String email;
    private final Long cpf;
    private final Integer rg;
    private final String cnpj;
    private final String servicoSocial;
    private final String profissao;
    private final String admObra;
    private final String telefone;
    private final String telefoneFixo;
    private final String descricao;
    private final String acabamento;
    private final String tubos;
    private final Boolean revestimento;
    private final BigDecimal valorTotal;
    private final Integer prazoMontagem;
    private final Integer numero;
    private final String bairro;
    private final String municipio;
    private final Integer cep;
    private final String referencia;
    private final Integer numeroCliente;
    private final String bairroCliente;
    private final String municipioCliente;
    private final Integer cepCliente;
    private final String referenciaCliente;
    private final LocalDateTime dataCadastro;
    private final Boolean flagOculto;
    private final BigDecimal valor;

    public PedidoResumo(
            Long id,
            Integer numeroPedido,
            String nomeCliente,
            String email,
            Long cpf,
            Integer rg,
            String cnpj,
            String servicoSocial,
            String profissao,
            String admObra,
            String telefone,
            String telefoneFixo,
            String descricao,
            String acabamento,
            String tubos,
            Boolean revestimento,
            BigDecimal valorTotal,
            Integer prazoMontagem,
            Integer numero,
            String bairro,
            String municipio,
            Integer cep,
            String referencia,
            Integer numeroCliente,
            String bairroCliente,
            String municipioCliente,
            Integer cepCliente,
            String referenciaCliente,
            LocalDateTime dataCadastro,
            Boolean flagOculto,
            BigDecimal valor
    ) {
        this.id = id;
        this.numeroPedido = numeroPedido;
        this.nomeCliente = nomeCliente;
        this.email = email;
        this.cpf = cpf;
        this.rg = rg;
        this.cnpj = cnpj;
        this.servicoSocial = servicoSocial;
        this.profissao = profissao;
        this.admObra = admObra;
        this.telefone = telefone;
        this.telefoneFixo = telefoneFixo;
        this.descricao = descricao;
        this.acabamento = acabamento;
        this.tubos = tubos;
        this.revestimento = revestimento;
        this.valorTotal = valorTotal;
        this.prazoMontagem = prazoMontagem;
        this.numero = numero;
        this.bairro = bairro;
        this.municipio = municipio;
        this.cep = cep;
        this.referencia = referencia;
        this.numeroCliente = numeroCliente;
        this.bairroCliente = bairroCliente;
        this.municipioCliente = municipioCliente;
        this.cepCliente = cepCliente;
        this.referenciaCliente = referenciaCliente;
        this.dataCadastro = dataCadastro;
        this.flagOculto = flagOculto;
        this.valor = valor;
    }

    public Long getId() {
        return id;
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
        return flagOculto;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
