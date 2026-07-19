package com.example.demo.pedidos.dto;
import com.example.demo.pedidos.model.PedidoEntity;
import com.example.demo.shared.util.FormatacaoUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record PedidoDTO(
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
    Integer numero,
    Integer estadoId,
    Integer municipioId,
    Integer bairroId,
    String bairro,
    String municipio,
    Integer cep,
    String referencia,
    Integer numeroCliente,
    Integer estadoClienteId,
    Integer municipioClienteId,
    Integer bairroClienteId,
    String bairroCliente,
    String municipioCliente,
    Integer cepCliente,
    String referenciaCliente,
    LocalDateTime dataCadastro,
    Boolean flagOculto,
    BigDecimal valor
) {
    public PedidoDTO(PedidoEntity pedidoEntity) {
        this(
            pedidoEntity.getId(),
            pedidoEntity.getNumeroPedido(),
            pedidoEntity.getNomeCliente(),
            pedidoEntity.getEmail(),
            pedidoEntity.getCpf(),
            pedidoEntity.getRg(),
            pedidoEntity.getCnpj(),
            pedidoEntity.getServicoSocial(),
            pedidoEntity.getProfissao(),
            pedidoEntity.getAdmObra(),
            pedidoEntity.getTelefone(),
            pedidoEntity.getTelefoneFixo(),
            pedidoEntity.getDescricao(),
            pedidoEntity.getAcabamento(),
            pedidoEntity.getTubos(),
            pedidoEntity.getRevestimento(),
            pedidoEntity.getValorTotal(),
            pedidoEntity.getPrazoMontagem(),
            pedidoEntity.getNumero(),
            pedidoEntity.getEstadoId(),
            pedidoEntity.getMunicipioId(),
            pedidoEntity.getBairroId(),
            pedidoEntity.getBairro(),
            pedidoEntity.getMunicipio(),
            pedidoEntity.getCep(),
            pedidoEntity.getReferencia(),
            pedidoEntity.getNumeroCliente(),
            pedidoEntity.getEstadoClienteId(),
            pedidoEntity.getMunicipioClienteId(),
            pedidoEntity.getBairroClienteId(),
            pedidoEntity.getBairroCliente(),
            pedidoEntity.getMunicipioCliente(),
            pedidoEntity.getCepCliente(),
            pedidoEntity.getReferenciaCliente(),
            pedidoEntity.getDataCadastro() == null
                    ? null
                    : LocalDateTime.ofInstant(pedidoEntity.getDataCadastro(), ZoneId.systemDefault()),
            pedidoEntity.getFlagOculto(),
            pedidoEntity.getValor()
        );
    }

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
    public Integer getNumero() { return numero; }
    public Integer getEstadoId() { return estadoId; }
    public Integer getMunicipioId() { return municipioId; }
    public Integer getBairroId() { return bairroId; }
    public String getBairro() { return bairro; }
    public String getMunicipio() { return municipio; }
    public Integer getCep() { return cep; }
    public String getReferencia() { return referencia; }
    public Integer getNumeroCliente() { return numeroCliente; }
    public Integer getEstadoClienteId() { return estadoClienteId; }
    public Integer getMunicipioClienteId() { return municipioClienteId; }
    public Integer getBairroClienteId() { return bairroClienteId; }
    public String getBairroCliente() { return bairroCliente; }
    public String getMunicipioCliente() { return municipioCliente; }
    public Integer getCepCliente() { return cepCliente; }
    public String getReferenciaCliente() { return referenciaCliente; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public Boolean getFlagOculto() { return flagOculto; }
    public BigDecimal getValor() { return valor; }

    public String getCpfFormatado() { return FormatacaoUtil.formatarCpf(cpf); }
    public String getRgFormatado() { return FormatacaoUtil.formatarRg(rg); }
    public String getCnpjFormatado() { return FormatacaoUtil.formatarCnpj(cnpj); }
    public String getTelefoneFormatado() { return FormatacaoUtil.formatarTelefoneCelular(telefone); }
    public String getTelefoneFixoFormatado() { return FormatacaoUtil.formatarTelefoneFixo(telefoneFixo); }
    public String getValorTotalFormatado() { return FormatacaoUtil.formatarValor(valorTotal); }
    public String getValorFormatado() { return FormatacaoUtil.formatarValor(valor); }
    public String getDataCadastroFormatado() { return FormatacaoUtil.formatarDataHora(dataCadastro); }
}