package com.example.demo.orcamentos.config;

import com.example.demo.orcamentos.dto.OrcamentoDTO;
import com.example.demo.shared.crud.listagem.ColunaConfig;
import java.util.List;

public class ListagemOrcamentosViewConfig {

    public static List<ColunaConfig<OrcamentoDTO>> obterConfiguracaoColunas() {
        return List.of(
                new ColunaConfig<>("id", "ID", OrcamentoDTO::getId),
                new ColunaConfig<>("nome", "Nome", OrcamentoDTO::getNome),
                new ColunaConfig<>("bairro", "Bairro", OrcamentoDTO::getBairro),
                new ColunaConfig<>("descricao", "Descrição", OrcamentoDTO::getDescricao),
                new ColunaConfig<>("flagEncerrado", "Encerrado", o -> o.getFlagEncerrado() != null && o.getFlagEncerrado() ? "Sim" : "Não"),
                new ColunaConfig<>("dataCadastroFormatado", "Data Cadastro", OrcamentoDTO::getDataCadastroFormatado)
        );
    }
}
