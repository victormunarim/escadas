package com.example.demo.pedidos.model;

import com.example.demo.orcamentos.model.OrcamentoEntity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pedidos", schema = "escadas")
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido", nullable = false)
    private Integer id;

    @Column(name = "acabamento")
    private String acabamento;

    @Column(name = "adm_obra")
    private String admObra;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "cpf")
    private Long cpf;

    @Column(name = "data_cadastro")
    private Instant dataCadastro;

    @Lob
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "email")
    private String email;

    @Column(name = "flag_oculto")
    private Boolean flagOculto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orcamento_id")
    private OrcamentoEntity orcamento;

    @Column(name = "cliente_nome")
    private String clienteNome;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "numero_cliente")
    private Integer numeroCliente;

    @Column(name = "numero_pedido")
    private Integer numeroPedido;

    @Column(name = "prazo_montagem")
    private Integer prazoMontagem;

    @Column(name = "profissao")
    private String profissao;

    @Column(name = "revestimento")
    private Boolean revestimento;

    @Column(name = "rg")
    private Integer rg;

    @Column(name = "servico_social")
    private String servicoSocial;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "telefone_fixo")
    private String telefoneFixo;

    @Column(name = "tubos")
    private String tubos;

    @Column(name = "valor", precision = 38, scale = 2)
    private BigDecimal valor;

    @Column(name = "valor_total", precision = 38, scale = 2)
    private BigDecimal valorTotal;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private EnderecosEntity endereco;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAcabamento() {
        return acabamento;
    }

    public void setAcabamento(String acabamento) {
        this.acabamento = acabamento;
    }

    public String getAdmObra() {
        return admObra;
    }

    public void setAdmObra(String admObra) {
        this.admObra = admObra;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getFlagOculto() {
        return flagOculto;
    }

    public void setFlagOculto(Boolean flagOculto) {
        this.flagOculto = flagOculto;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getNomeCliente() {
        return clienteNome;
    }

    public void setNomeCliente(String nomeCliente) {
        this.clienteNome = nomeCliente;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(Integer numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Integer getPrazoMontagem() {
        return prazoMontagem;
    }

    public void setPrazoMontagem(Integer prazoMontagem) {
        this.prazoMontagem = prazoMontagem;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public Boolean getRevestimento() {
        return revestimento;
    }

    public void setRevestimento(Boolean revestimento) {
        this.revestimento = revestimento;
    }

    public Integer getRg() {
        return rg;
    }

    public void setRg(Integer rg) {
        this.rg = rg;
    }

    public String getServicoSocial() {
        return servicoSocial;
    }

    public void setServicoSocial(String servicoSocial) {
        this.servicoSocial = servicoSocial;
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

    public String getTubos() {
        return tubos;
    }

    public void setTubos(String tubos) {
        this.tubos = tubos;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public EnderecosEntity getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecosEntity endereco) {
        this.endereco = endereco;
    }

    public Integer getEstadoId() {
        return endereco == null ? null : endereco.getEstadoId();
    }

    public void setEstadoId(Integer estadoId) {
        ensureEndereco();
        endereco.setEstadoId(estadoId);
    }

    public Integer getEstadoClienteId() {
        return endereco == null ? null : endereco.getEstadoClienteId();
    }

    public void setEstadoClienteId(Integer estadoClienteId) {
        ensureEndereco();
        endereco.setEstadoClienteId(estadoClienteId);
    }

    public Integer getMunicipioId() {
        return endereco == null ? null : endereco.getMunicipioId();
    }

    public void setMunicipioId(Integer municipioId) {
        ensureEndereco();
        endereco.setMunicipioId(municipioId);
    }

    public Integer getMunicipioClienteId() {
        return endereco == null ? null : endereco.getMunicipioClienteId();
    }

    public void setMunicipioClienteId(Integer municipioClienteId) {
        ensureEndereco();
        endereco.setMunicipioClienteId(municipioClienteId);
    }

    public Integer getBairroId() {
        return endereco == null ? null : endereco.getBairroId();
    }

    public void setBairroId(Integer bairroId) {
        ensureEndereco();
        endereco.setBairroId(bairroId);
    }

    public Integer getBairroClienteId() {
        return endereco == null ? null : endereco.getBairroClienteId();
    }

    public void setBairroClienteId(Integer bairroClienteId) {
        ensureEndereco();
        endereco.setBairroClienteId(bairroClienteId);
    }

    public String getBairro() {
        return endereco == null ? null : String.valueOf(endereco.getBairroId());
    }

    public void setBairro(String bairro) {
        ensureEndereco();
        endereco.setBairroId(parseInteger(bairro));
    }

    public String getMunicipio() {
        return endereco == null ? null : String.valueOf(endereco.getMunicipioId());
    }

    public void setMunicipio(String municipio) {
        ensureEndereco();
        endereco.setMunicipioId(parseInteger(municipio));
    }

    public Integer getCep() {
        return endereco == null ? null : endereco.getCep();
    }

    public void setCep(Integer cep) {
        ensureEndereco();
        endereco.setCep(cep);
    }

    public String getReferencia() {
        return endereco == null ? null : endereco.getReferencia();
    }

    public void setReferencia(String referencia) {
        ensureEndereco();
        endereco.setReferencia(referencia);
    }

    public Integer getNumeroClienteEndereco() {
        return endereco == null ? null : endereco.getCepClienteId();
    }

    public void setCepCliente(Integer cepCliente) {
        ensureEndereco();
        endereco.setCepClienteId(cepCliente);
    }

    public String getBairroCliente() {
        return endereco == null ? null : String.valueOf(endereco.getBairroClienteId());
    }

    public void setBairroCliente(String bairroCliente) {
        ensureEndereco();
        endereco.setBairroClienteId(parseInteger(bairroCliente));
    }

    public String getMunicipioCliente() {
        return endereco == null ? null : String.valueOf(endereco.getMunicipioClienteId());
    }

    public void setMunicipioCliente(String municipioCliente) {
        ensureEndereco();
        endereco.setMunicipioClienteId(parseInteger(municipioCliente));
    }

    public Integer getCepCliente() {
        return endereco == null ? null : endereco.getCepClienteId();
    }

    public void setReferenciaCliente(String referenciaCliente) {
        ensureEndereco();
        endereco.setReferenciaCliente(referenciaCliente);
    }

    public String getReferenciaCliente() {
        return endereco == null ? null : endereco.getReferenciaCliente();
    }

    private void ensureEndereco() {
        if (this.endereco == null) {
            this.endereco = new EnderecosEntity();
            this.endereco.setPedido(this);
        }
    }

    private Integer parseInteger(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @PrePersist
    private void preencherDataCadastro() {
        if (this.dataCadastro == null) {
            this.dataCadastro = Instant.now();
        }
    }

    public OrcamentoEntity getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(OrcamentoEntity orcamento) {
        this.orcamento = orcamento;
    }
}