package com.example.demo.tarefas.config;

import com.example.demo.shared.crud.OpcaoCrud;
import com.example.demo.shared.crud.formulario.CampoFormularioCrud;
import com.example.demo.shared.crud.formulario.CamposFormularioCrud;
import com.example.demo.tarefas.dto.FormularioTarefaDTO;
import java.util.List;

public class FormularioTarefaViewConfig {

    public static List<CampoFormularioCrud<FormularioTarefaDTO>> criarCampos(
            List<OpcaoCrud> opcoesTipoTarefa,
            List<OpcaoCrud> opcoesResponsavel
    ) {
        return List.of(
                CamposFormularioCrud.selecao(
                        "tipoTarefaId",
                        "Tipo de Tarefa",
                        true,
                        opcoesTipoTarefa,
                        "campo--tipo-tarefa",
                        FormularioTarefaDTO::getTipoTarefaId
                ),
                CamposFormularioCrud.selecao(
                        "responsavelId",
                        "Responsável",
                        true,
                        opcoesResponsavel,
                        "campo--responsavel",
                        FormularioTarefaDTO::getResponsavelId
                ),
                CamposFormularioCrud.textarea(
                        "descricao",
                        "Descrição",
                        "Descreva detalhadamente a tarefa",
                        true,
                        4,
                        1000,
                        "campo--descricao",
                        FormularioTarefaDTO::getDescricao
                )
        );
    }
}
