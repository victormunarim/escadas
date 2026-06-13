package com.example.demo.pedidos.dto;
import com.example.demo.pedidos.model.PedidoResumoEntity;
import com.example.demo.pedidos.config.ColunasPedido;

import com.example.demo.shared.util.FormatacaoUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public record PedidoResumoDTO(
    Integer id,
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
    Instant dataCadastro,
    BigDecimal valor
) {
    private static final DateTimeFormatter FORMATO_DATA_CADASTRO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PedidoResumoDTO(PedidoResumoEntity pedido) {
        this(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getNomeCliente(),
            pedido.getEmail(),
            pedido.getCpf(),
            pedido.getRg(),
            pedido.getCnpj(),
            pedido.getServicoSocial(),
            pedido.getProfissao(),
            pedido.getAdmObra(),
            pedido.getTelefone(),
            pedido.getTelefoneFixo(),
            pedido.getDescricao(),
            pedido.getAcabamento(),
            pedido.getTubos(),
            pedido.getRevestimento(),
            pedido.getValorTotal(),
            pedido.getPrazoMontagem(),
            pedido.getDataCadastro(),
            pedido.getValor()
        );
    }

    public Object get(String campo) {
        if (campo == null) {
            return null;
        }
        return switch (campo) {
            case ColunasPedido.ID_PEDIDO -> id();
            case ColunasPedido.NUMERO_PEDIDO -> numeroPedido();
            case ColunasPedido.CLIENTE_NOME -> nomeCliente();
            case ColunasPedido.EMAIL -> email();
            case ColunasPedido.CPF -> FormatacaoUtil.formatarCpf(cpf());
            case ColunasPedido.RG -> FormatacaoUtil.formatarRg(rg());
            case ColunasPedido.CNPJ -> cnpj();
            case ColunasPedido.SERVICO_SOCIAL -> servicoSocial();
            case ColunasPedido.PROFISSAO -> profissao();
            case ColunasPedido.ADM_OBRA -> admObra();
            case ColunasPedido.TELEFONE -> FormatacaoUtil.formatarTelefoneCelular(telefone());
            case ColunasPedido.TELEFONE_FIXO -> FormatacaoUtil.formatarTelefoneFixo(telefoneFixo());
            case ColunasPedido.DESCRICAO -> descricao();
            case ColunasPedido.ACABAMENTO -> acabamento();
            case ColunasPedido.TUBOS -> tubos();
            case ColunasPedido.REVESTIMENTO -> Boolean.TRUE.equals(revestimento()) ? "Sim" : "Não";
            case ColunasPedido.VALOR_TOTAL -> FormatacaoUtil.formatarValor(valorTotal());
            case ColunasPedido.PRAZO_MONTAGEM -> prazoMontagem();
            case ColunasPedido.DATA_CADASTRO ->
                    dataCadastro() == null ? "" : dataCadastro().atZone(ZoneId.systemDefault()).toLocalDate().format(FORMATO_DATA_CADASTRO);
            case ColunasPedido.VALOR -> FormatacaoUtil.formatarValor(valor());
            default -> null;
        };
    }

    // Getter JavaBeans para compatibilidade com Thymeleaf e frameworks legados
    public Integer getId() { return id; }
    public Integer getNumeroPedido() { return numeroPedido; }
    public String getNomeCliente() { return nomeCliente; }
    public String getEmail() { return email; }
    public Long getCpf() { return cpf; }
    public Integer getRg() { return rg; }
    public String getCnpj() { return cnpj; }
    public String getServicoSocial() { return servicoSocial; }
    public String getProfissao() { return profissao; }
    public String getAdmObra() { return admObra; }
    public String getTelefone() { return telefone; }
    public String getTelefoneFixo() { return telefoneFixo; }
    public String getDescricao() { return descricao; }
    public String getAcabamento() { return acabamento; }
    public String getTubos() { return tubos; }
    public Boolean getRevestimento() { return revestimento; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public Integer getPrazoMontagem() { return prazoMontagem; }
    public Instant getDataCadastro() { return dataCadastro; }
    public BigDecimal getValor() { return valor; }
}