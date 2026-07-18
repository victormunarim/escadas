package com.example.demo.pedidos.config;

import com.example.demo.pedidos.dto.PedidoDTO;
import com.example.demo.shared.crud.listagem.ColunaConfig;
import java.util.List;

public class ListagemPedidosViewConfig {

    public static List<ColunaConfig<PedidoDTO>> obterConfiguracaoColunas() {
        return List.of(
                new ColunaConfig<>(ColunasPedido.CAMPO_ID_PEDIDO, ColunasPedido.LABEL_ID_PEDIDO, PedidoDTO::id),
                new ColunaConfig<>("orcamentoId", "ID Orçamento", p -> p.getOrcamentoId() == null ? "-" : String.valueOf(p.getOrcamentoId())),
                new ColunaConfig<>(ColunasPedido.CAMPO_NUMERO_PEDIDO, ColunasPedido.LABEL_NUMERO_PEDIDO, PedidoDTO::numeroPedido),
                new ColunaConfig<>(ColunasPedido.CAMPO_CLIENTE_NOME, ColunasPedido.LABEL_CLIENTE_NOME, PedidoDTO::nomeCliente),
                new ColunaConfig<>(ColunasPedido.CAMPO_EMAIL, ColunasPedido.LABEL_EMAIL, PedidoDTO::email),
                new ColunaConfig<>("cpfFormatado", ColunasPedido.LABEL_CPF, PedidoDTO::getCpfFormatado),
                new ColunaConfig<>("rgFormatado", ColunasPedido.LABEL_RG, PedidoDTO::getRgFormatado),
                new ColunaConfig<>("cnpjFormatado", ColunasPedido.LABEL_CNPJ, PedidoDTO::getCnpjFormatado),
                new ColunaConfig<>(ColunasPedido.CAMPO_SERVICO_SOCIAL, ColunasPedido.LABEL_SERVICO_SOCIAL, PedidoDTO::servicoSocial),
                new ColunaConfig<>(ColunasPedido.CAMPO_PROFISSAO, ColunasPedido.LABEL_PROFISSAO, PedidoDTO::profissao),
                new ColunaConfig<>(ColunasPedido.CAMPO_ADM_OBRA, ColunasPedido.LABEL_ADM_OBRA, PedidoDTO::admObra),
                new ColunaConfig<>("telefoneFormatado", ColunasPedido.LABEL_TELEFONE, PedidoDTO::getTelefoneFormatado),
                new ColunaConfig<>("telefoneFixoFormatado", ColunasPedido.LABEL_TELEFONE_FIXO, PedidoDTO::getTelefoneFixoFormatado),
                new ColunaConfig<>(ColunasPedido.CAMPO_DESCRICAO, ColunasPedido.LABEL_DESCRICAO, PedidoDTO::descricao),
                new ColunaConfig<>(ColunasPedido.CAMPO_ACABAMENTO, ColunasPedido.LABEL_ACABAMENTO, PedidoDTO::acabamento),
                new ColunaConfig<>(ColunasPedido.CAMPO_TUBOS, ColunasPedido.LABEL_TUBOS, PedidoDTO::tubos),
                new ColunaConfig<>(ColunasPedido.CAMPO_REVESTIMENTO, ColunasPedido.LABEL_REVESTIMENTO, p -> p.revestimento() != null && p.revestimento() ? "Sim" : "Não"),
                new ColunaConfig<>("valorTotalFormatado", ColunasPedido.LABEL_VALOR_TOTAL, PedidoDTO::getValorTotalFormatado),
                new ColunaConfig<>(ColunasPedido.CAMPO_PRAZO_MONTAGEM, ColunasPedido.LABEL_PRAZO_MONTAGEM, PedidoDTO::prazoMontagem),
                new ColunaConfig<>("dataCadastroFormatado", ColunasPedido.LABEL_DATA_CADASTRO, PedidoDTO::getDataCadastroFormatado),
                new ColunaConfig<>("valorFormatado", ColunasPedido.LABEL_VALOR, PedidoDTO::getValorFormatado)
        );
    }
}
