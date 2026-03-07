package com.example.demo.ENUM;

import com.example.demo.entidades.Pedido;

import java.util.function.Function;

public enum PedidoColuna {

    ID_PEDIDO("id_pedido", Pedido::getId),
    NUMERO_PEDIDO("numero_pedido", Pedido::getNumeroPedido),
    CLIENTE_NOME("cliente_nome", Pedido::getClienteNome),
    EMAIL("email", Pedido::getEmail),
    CPF("cpf", Pedido::getCpf),
    RG("rg", Pedido::getRg),
    CNPJ("cnpj", Pedido::getCnpj),
    SERVICO_SOCIAL("servico_social", Pedido::getServicoSocial),
    PROFISSAO("profissao", Pedido::getProfissao),
    ADM_OBRA("adm_obra", Pedido::getAdmObra),
    TELEFONE("telefone", Pedido::getTelefone),
    TELEFONE_FIXO("telefone_fixo", Pedido::getTelefoneFixo),
    DESCRICAO("descricao", Pedido::getDescricao),
    ACABAMENTO("acabamento", Pedido::getAcabamento),
    TUBOS("tubos", Pedido::getTubos),
    REVESTIMENTO("revestimento", Pedido::getRevestimento),
    VALOR_TOTAL("valor_total", Pedido::getValorTotal),
    PRAZO_MONTAGEM("prazo_montagem", Pedido::getPrazoMontagem),
    NUMERO("numero", Pedido::getNumero),
    BAIRRO("bairro", Pedido::getBairro),
    CIDADE("cidade", Pedido::getCidade),
    CEP("cep", Pedido::getCep),
    REFERENCIA("referencia", Pedido::getReferencia),
    NUMERO_CLIENTE("numero_cliente", Pedido::getNumeroCliente),
    BAIRRO_CLIENTE("bairro_cliente", Pedido::getBairroCliente),
    CIDADE_CLIENTE("cidade_cliente", Pedido::getCidadeCliente),
    CEP_CLIENTE("cep_cliente", Pedido::getCepCliente),
    REFERENCIA_CLIENTE("referencia_cliente", Pedido::getReferenciaCliente),
    FLAG_OCULTO("flag_oculto", Pedido::getFlagOculto),
    CLIENTE("cliente", Pedido::getCliente),
    VALOR("valor", Pedido::getValor);

    private final String coluna;
    private final Function<Pedido, Object> extractor;

    PedidoColuna(String coluna, Function<Pedido, Object> extractor) {
        this.coluna = coluna;
        this.extractor = extractor;
    }

    public String getColuna() {
        return coluna;
    }

    public Object extract(Pedido pedido) {
        return extractor.apply(pedido);
    }
}