package com.example.demo.orcamentos.config;

import com.example.demo.orcamentos.dto.OrcamentoDTO;
import com.example.demo.shared.crud.listagem.ColunaConfig;
import java.util.List;

public class ListagemOrcamentosViewConfig {

    public static List<ColunaConfig<OrcamentoDTO>> obterConfiguracaoColunas() {
        return obterConfiguracaoColunas(false);
    }

    public static List<ColunaConfig<OrcamentoDTO>> obterConfiguracaoColunas(boolean ehTecnico) {
        if (ehTecnico) {
            return List.of(
                    new ColunaConfig<>("id", "ID", OrcamentoDTO::getId),
                    new ColunaConfig<>("nome", "Nome", OrcamentoDTO::getNome),
                    new ColunaConfig<>("bairro", "Bairro", OrcamentoDTO::getBairro),
                    new ColunaConfig<>("descricao", "Descrição", OrcamentoDTO::getDescricao),
                    new ColunaConfig<>(
                            "flagEncerrado", "Encerrado",
                            o -> o.getFlagEncerrado() != null && o.getFlagEncerrado() ? "Sim" : "Não"
                    ),
                    new ColunaConfig<>(
                            "dataCadastroFormatado", "Data Cadastro", OrcamentoDTO::getDataCadastroFormatado
                    )
            );
        }
        return List.of(
                new ColunaConfig<>("id", "ID", OrcamentoDTO::getId),
                new ColunaConfig<>("nome", "Nome", OrcamentoDTO::getNome),
                new ColunaConfig<>(
                        "etiquetaNome", "Etiqueta",
                        o -> o.getEtiquetaNome() == null || o.getEtiquetaNome().isBlank() ? "-" : o.getEtiquetaNome()
                ),
                new ColunaConfig<>("bairro", "Bairro", OrcamentoDTO::getBairro),
                new ColunaConfig<>("descricao", "Descrição", OrcamentoDTO::getDescricao),
                new ColunaConfig<>(
                        "flagEncerrado", "Encerrado",
                        o -> o.getFlagEncerrado() != null && o.getFlagEncerrado() ? "Sim" : "Não"
                ),
                new ColunaConfig<>(
                        "dataCadastroFormatado", "Data Cadastro", OrcamentoDTO::getDataCadastroFormatado
                )
        );
    }
}
