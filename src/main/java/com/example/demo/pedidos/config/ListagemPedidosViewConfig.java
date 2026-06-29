package com.example.demo.pedidos.config;

import com.example.demo.pedidos.dto.PedidoDTO;
import com.example.demo.shared.crud.listagem.ColunaConfig;
import java.util.List;

public class ListagemPedidosViewConfig {

    public static List<ColunaConfig<PedidoDTO>> obterConfiguracaoColunas() {
        return List.of(
                new ColunaConfig<>("id", "ID", PedidoDTO::id),
                new ColunaConfig<>("orcamentoId", "ID Orçamento", p -> p.getOrcamentoId() == null ? "-" : String.valueOf(p.getOrcamentoId())),
                new ColunaConfig<>("numeroPedido", "Número do Pedido", PedidoDTO::numeroPedido),
                new ColunaConfig<>("nomeCliente", "Cliente Nome", PedidoDTO::nomeCliente),
                new ColunaConfig<>("email", "Email", PedidoDTO::email),
                new ColunaConfig<>("cpfFormatado", "CPF", PedidoDTO::getCpfFormatado),
                new ColunaConfig<>("rgFormatado", "RG", PedidoDTO::getRgFormatado),
                new ColunaConfig<>("cnpjFormatado", "CNPJ", PedidoDTO::getCnpjFormatado),
                new ColunaConfig<>("servicoSocial", "Serviço Social", PedidoDTO::servicoSocial),
                new ColunaConfig<>("profissao", "Profissão", PedidoDTO::profissao),
                new ColunaConfig<>("admObra", "Adm. Obra", PedidoDTO::admObra),
                new ColunaConfig<>("telefoneFormatado", "Telefone", PedidoDTO::getTelefoneFormatado),
                new ColunaConfig<>("telefoneFixoFormatado", "Telefone Fixo", PedidoDTO::getTelefoneFixoFormatado),
                new ColunaConfig<>("descricao", "Descrição", PedidoDTO::descricao),
                new ColunaConfig<>("acabamento", "Acabamento", PedidoDTO::acabamento),
                new ColunaConfig<>("tubos", "Tubos", PedidoDTO::tubos),
                new ColunaConfig<>("revestimento", "Revestimento", p -> p.revestimento() != null && p.revestimento() ? "Sim" : "Não"),
                new ColunaConfig<>("valorTotalFormatado", "Valor Total", PedidoDTO::getValorTotalFormatado),
                new ColunaConfig<>("prazoMontagem", "Prazo Montagem", PedidoDTO::prazoMontagem),
                new ColunaConfig<>("dataCadastroFormatado", "Data Cadastro", PedidoDTO::getDataCadastroFormatado),
                new ColunaConfig<>("valorFormatado", "Valor", PedidoDTO::getValorFormatado)
        );
    }
}
