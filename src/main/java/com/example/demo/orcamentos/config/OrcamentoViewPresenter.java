package com.example.demo.orcamentos.config;

import com.example.demo.localidades.service.ConsultaLocalidadesService;
import com.example.demo.orcamentos.dto.OrcamentoDTO;
import com.example.demo.pedidos.dto.PedidoDTO;
import com.example.demo.pedidos.service.PedidoService;
import com.example.demo.shared.arquivos.dto.ArquivoDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrcamentoViewPresenter {

    public static Map<String, Object> montarVisualizacao(
            OrcamentoDTO orcamento,
            List<ArquivoDTO> arquivos,
            PedidoService pedidoService,
            ConsultaLocalidadesService localidadeService
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", orcamento.getId());
        response.put("nome", orcamento.getNome());
        response.put("bairro", orcamento.getBairro());
        response.put("descricao", orcamento.getDescricao());
        response.put("flagEncerrado", orcamento.getFlagEncerrado());
        response.put("etiquetaId", orcamento.getEtiquetaId());
        response.put("etiquetaNome", orcamento.getEtiquetaNome());
        response.put("pedidoId", orcamento.getPedidoId());
        response.put("pedidoNumero", orcamento.getPedidoNumero());
        response.put("pedidoCliente", orcamento.getPedidoCliente());
        response.put("flagTecnico", orcamento.getFlagTecnico());

        Map<String, String> detalhes = new HashMap<>();
        detalhes.put("Nome", orcamento.getNome());
        detalhes.put("Etiqueta", orcamento.getEtiquetaNome() == null || orcamento.getEtiquetaNome().isBlank() ? "-" : orcamento.getEtiquetaNome());
        detalhes.put("Bairro", orcamento.getBairro() == null || orcamento.getBairro().isBlank() ? "-" : orcamento.getBairro());
        detalhes.put("Descrição", orcamento.getDescricao() == null || orcamento.getDescricao().isBlank() ? "-" : orcamento.getDescricao());
        detalhes.put("Data de Cadastro", orcamento.getDataCadastroFormatado());

        if (orcamento.getPedidoId() != null && pedidoService != null) {
            try {
                PedidoDTO pedido = pedidoService.buscarPorId(Long.valueOf(orcamento.getPedidoId()));
                detalhes.put("Pedido Associado", "#" + pedido.getNumeroPedido() + " - " + pedido.getNomeCliente());
            } catch (Exception ignored) {
            }
        }

        response.put("detalhes", detalhes);
        response.put("arquivos", arquivos);

        return response;
    }
}
