package com.example.demo.pedidos.config;
import com.example.demo.pedidos.dto.PedidoDTO;

import com.example.demo.shared.util.FormatacaoUtil;
import com.example.demo.localidades.service.ConsultaLocalidadesService;
import java.util.LinkedHashMap;
import java.util.Map;

public class PedidoViewPresenter {

    public static Map<String, String> montarResumoPedido(PedidoDTO pedido) {
        Map<String, String> resumoPedido = new LinkedHashMap<>();
        resumoPedido.put("ID do Pedido", FormatacaoUtil.formatarTexto(pedido.getId()));
        resumoPedido.put("Número do Pedido", FormatacaoUtil.formatarTexto(pedido.getNumeroPedido()));
        resumoPedido.put("Data de Cadastro", FormatacaoUtil.formatarDataHora(pedido.getDataCadastro()));
        resumoPedido.put("Valor", FormatacaoUtil.formatarValor(pedido.getValor()));
        resumoPedido.put("Valor Total", FormatacaoUtil.formatarValor(pedido.getValorTotal()));
        resumoPedido.put("Prazo de Montagem", FormatacaoUtil.formatarTexto(pedido.getPrazoMontagem()));
        resumoPedido.put("Acabamento", FormatacaoUtil.formatarTexto(pedido.getAcabamento()));
        resumoPedido.put("Tubos", FormatacaoUtil.formatarTexto(pedido.getTubos()));
        resumoPedido.put("Revestimento", FormatacaoUtil.formatarSimNao(pedido.getRevestimento()));
        resumoPedido.put("Descrição", FormatacaoUtil.formatarTexto(pedido.getDescricao()));
        return resumoPedido;
    }

    public static Map<String, String> montarDadosCliente(PedidoDTO pedido) {
        Map<String, String> dadosCliente = new LinkedHashMap<>();
        dadosCliente.put("Nome", FormatacaoUtil.formatarTexto(pedido.getNomeCliente()));
        dadosCliente.put("E-mail", FormatacaoUtil.formatarTexto(pedido.getEmail()));
        dadosCliente.put("CPF", FormatacaoUtil.formatarCpf(pedido.getCpf()));
        dadosCliente.put("RG", FormatacaoUtil.formatarRg(pedido.getRg()));
        dadosCliente.put("CNPJ", FormatacaoUtil.formatarCnpj(pedido.getCnpj()));
        dadosCliente.put("Serviço Social", FormatacaoUtil.formatarTexto(pedido.getServicoSocial()));
        dadosCliente.put("Profissão", FormatacaoUtil.formatarTexto(pedido.getProfissao()));
        dadosCliente.put("Adm. Obra", FormatacaoUtil.formatarTexto(pedido.getAdmObra()));
        dadosCliente.put("Telefone", FormatacaoUtil.formatarTelefoneCelular(pedido.getTelefone()));
        dadosCliente.put("Telefone Fixo", FormatacaoUtil.formatarTelefoneFixo(pedido.getTelefoneFixo()));
        return dadosCliente;
    }

    public static Map<String, String> montarEnderecoObra(PedidoDTO pedido, ConsultaLocalidadesService localidadeService) {
        Map<String, String> enderecoObra = new LinkedHashMap<>();
        enderecoObra.put("Estado", localidadeService.resolverEstado(pedido.getEstadoId()));
        enderecoObra.put("Município", localidadeService.resolverMunicipio(pedido.getMunicipioId()));
        enderecoObra.put("Bairro", localidadeService.resolverBairro(pedido.getBairroId()));
        enderecoObra.put("CEP", FormatacaoUtil.formatarCep(pedido.getCep()));
        enderecoObra.put("Referência", FormatacaoUtil.formatarTexto(pedido.getReferencia()));
        enderecoObra.put("Número", FormatacaoUtil.formatarTexto(pedido.getNumero()));
        return enderecoObra;
    }

    public static Map<String, String> montarEnderecoCliente(PedidoDTO pedido, ConsultaLocalidadesService localidadeService) {
        Map<String, String> enderecoCliente = new LinkedHashMap<>();
        enderecoCliente.put("Estado", localidadeService.resolverEstado(pedido.getEstadoClienteId()));
        enderecoCliente.put("Município", localidadeService.resolverMunicipio(pedido.getMunicipioClienteId()));
        enderecoCliente.put("Bairro", localidadeService.resolverBairro(pedido.getBairroClienteId()));
        enderecoCliente.put("CEP", FormatacaoUtil.formatarCep(pedido.getCepCliente()));
        enderecoCliente.put("Referência", FormatacaoUtil.formatarTexto(pedido.getReferenciaCliente()));
        enderecoCliente.put("Número", FormatacaoUtil.formatarTexto(pedido.getNumeroCliente()));
        return enderecoCliente;
    }
}