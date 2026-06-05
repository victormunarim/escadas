package com.example.demo.dto;

import com.example.demo.constants.ColunasPedido;
import com.example.demo.entity.PedidoResumoEntity;
import com.example.demo.util.FormatacaoUtil;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class PedidoResumoDTO {
    private static final DateTimeFormatter FORMATO_DATA_CADASTRO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
    private Instant dataCadastro;
    private BigDecimal valor;

    public PedidoResumoDTO() {
    }

    public PedidoResumoDTO(PedidoResumoEntity pedido) {
        BeanUtils.copyProperties(pedido, this);
    }

    public Object get(String campo) {
        if (campo == null) {
            return null;
        }
        return switch (campo) {
            case ColunasPedido.ID_PEDIDO -> getId();
            case ColunasPedido.NUMERO_PEDIDO -> getNumeroPedido();
            case ColunasPedido.CLIENTE_NOME -> getNomeCliente();
            case ColunasPedido.EMAIL -> getEmail();
            case ColunasPedido.CPF -> FormatacaoUtil.formatarCpf(getCpf());
            case ColunasPedido.RG -> FormatacaoUtil.formatarRg(getRg());
            case ColunasPedido.CNPJ -> getCnpj();
            case ColunasPedido.SERVICO_SOCIAL -> getServicoSocial();
            case ColunasPedido.PROFISSAO -> getProfissao();
            case ColunasPedido.ADM_OBRA -> getAdmObra();
            case ColunasPedido.TELEFONE -> FormatacaoUtil.formatarTelefoneCelular(getTelefone());
            case ColunasPedido.TELEFONE_FIXO -> FormatacaoUtil.formatarTelefoneFixo(getTelefoneFixo());
            case ColunasPedido.DESCRICAO -> getDescricao();
            case ColunasPedido.ACABAMENTO -> getAcabamento();
            case ColunasPedido.TUBOS -> getTubos();
            case ColunasPedido.REVESTIMENTO -> Boolean.TRUE.equals(getRevestimento()) ? "Sim" : "Não";
            case ColunasPedido.VALOR_TOTAL -> FormatacaoUtil.formatarValor(getValorTotal());
            case ColunasPedido.PRAZO_MONTAGEM -> getPrazoMontagem();
            case ColunasPedido.DATA_CADASTRO ->
                    getDataCadastro() == null ? "" : getDataCadastro().atZone(ZoneId.systemDefault()).toLocalDate().format(FORMATO_DATA_CADASTRO);
            case ColunasPedido.VALOR -> FormatacaoUtil.formatarValor(getValor());
            default -> null;
        };
    }

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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
