package com.example.demo.orcamentos.config;

import com.example.demo.shared.crud.OpcaoCrud;
import com.example.demo.shared.crud.formulario.CampoFormularioCrud;
import com.example.demo.shared.crud.formulario.CamposFormularioCrud;
import com.example.demo.orcamentos.dto.FormularioOrcamentoDTO;
import java.util.List;

public class FormularioOrcamentoViewConfig {

    public static List<CampoFormularioCrud<FormularioOrcamentoDTO>> criarCampos(
            List<OpcaoCrud> opcoesEtiqueta,
            List<OpcaoCrud> opcoesPedido
    ) {
        return List.of(
                CamposFormularioCrud.texto(
                        "nome",
                        "Nome",
                        "Nome do cliente ou do orçamento",
                        true,
                        120,
                        "campo--nome",
                        FormularioOrcamentoDTO::getNome
                ),
                CamposFormularioCrud.selecao(
                        "etiquetaId",
                        "Etiqueta",
                        false,
                        opcoesEtiqueta,
                        "campo--etiqueta",
                        FormularioOrcamentoDTO::getEtiquetaId
                ),
                CamposFormularioCrud.selecao(
                        "pedidoId",
                        "Pedido Associado",
                        false,
                        opcoesPedido,
                        "campo--pedido",
                        FormularioOrcamentoDTO::getPedidoId
                ),
                CamposFormularioCrud.texto(
                        "bairro",
                        "Bairro",
                        "Bairro do orçamento",
                        false,
                        120,
                        "campo--bairro",
                        FormularioOrcamentoDTO::getBairro
                ),
                CamposFormularioCrud.textarea(
                        "descricao",
                        "Descrição",
                        "Descrição detalhada do orçamento...",
                        false,
                        4,
                        2000,
                        "filtro-crud--completo campo--descricao",
                        FormularioOrcamentoDTO::getDescricao
                )
        );
    }
}
