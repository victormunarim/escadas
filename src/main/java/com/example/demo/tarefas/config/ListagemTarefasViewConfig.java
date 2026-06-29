package com.example.demo.tarefas.config;

import com.example.demo.tarefas.dto.TarefaDTO;
import com.example.demo.shared.crud.listagem.ColunaConfig;
import java.util.List;

public class ListagemTarefasViewConfig {

    public static List<ColunaConfig<TarefaDTO>> obterConfiguracaoColunas() {
        return List.of(
                new ColunaConfig<>("id", "ID", TarefaDTO::getId),
                new ColunaConfig<>("tipoTarefaNome", "Tipo de Tarefa", TarefaDTO::getTipoTarefaNome),
                new ColunaConfig<>("responsavelNome", "Responsável", TarefaDTO::getResponsavelNome),
                new ColunaConfig<>("descricao", "Descrição", t -> t.getDescricao() == null ? "" : t.getDescricao()),
                new ColunaConfig<>("flagConcluida", "Concluída", t -> t.getFlagConcluida() != null && t.getFlagConcluida() ? "Sim" : "Não"),
                new ColunaConfig<>("dataCadastroFormatado", "Data Cadastro", TarefaDTO::getDataCadastroFormatado),
                new ColunaConfig<>("extChave", "Chave Externa", t -> t.getExtChave() == null ? "-" : t.getExtChave()),
                new ColunaConfig<>("extId", "ID Externo", t -> t.getExtId() == null ? "-" : String.valueOf(t.getExtId()))
        );
    }
}
