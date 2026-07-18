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
        resumoPedido.put(ColunasPedido.LABEL_NUMERO_PEDIDO, FormatacaoUtil.formatarTexto(pedido.getNumeroPedido()));
        resumoPedido.put("Data de Cadastro", FormatacaoUtil.formatarDataHora(pedido.getDataCadastro()));
        resumoPedido.put(ColunasPedido.LABEL_VALOR, FormatacaoUtil.formatarValor(pedido.getValor()));
        resumoPedido.put(ColunasPedido.LABEL_VALOR_TOTAL, FormatacaoUtil.formatarValor(pedido.getValorTotal()));
        resumoPedido.put("Prazo de Montagem", FormatacaoUtil.formatarTexto(pedido.getPrazoMontagem()));
        resumoPedido.put(ColunasPedido.LABEL_ACABAMENTO, FormatacaoUtil.formatarTexto(pedido.getAcabamento()));
        resumoPedido.put(ColunasPedido.LABEL_TUBOS, FormatacaoUtil.formatarTexto(pedido.getTubos()));
        resumoPedido.put(ColunasPedido.LABEL_REVESTIMENTO, FormatacaoUtil.formatarSimNao(pedido.getRevestimento()));
        resumoPedido.put(ColunasPedido.LABEL_DESCRICAO, FormatacaoUtil.formatarTexto(pedido.getDescricao()));
        return resumoPedido;
    }

    public static Map<String, String> montarDadosCliente(PedidoDTO pedido) {
        Map<String, String> dadosCliente = new LinkedHashMap<>();
        dadosCliente.put("Nome", FormatacaoUtil.formatarTexto(pedido.getNomeCliente()));
        dadosCliente.put("E-mail", FormatacaoUtil.formatarTexto(pedido.getEmail()));
        dadosCliente.put(ColunasPedido.LABEL_CPF, FormatacaoUtil.formatarCpf(pedido.getCpf()));
        dadosCliente.put(ColunasPedido.LABEL_RG, FormatacaoUtil.formatarRg(pedido.getRg()));
        dadosCliente.put(ColunasPedido.LABEL_CNPJ, FormatacaoUtil.formatarCnpj(pedido.getCnpj()));
        dadosCliente.put(ColunasPedido.LABEL_SERVICO_SOCIAL, FormatacaoUtil.formatarTexto(pedido.getServicoSocial()));
        dadosCliente.put(ColunasPedido.LABEL_PROFISSAO, FormatacaoUtil.formatarTexto(pedido.getProfissao()));
        dadosCliente.put(ColunasPedido.LABEL_ADM_OBRA, FormatacaoUtil.formatarTexto(pedido.getAdmObra()));
        dadosCliente.put(ColunasPedido.LABEL_TELEFONE, FormatacaoUtil.formatarTelefoneCelular(pedido.getTelefone()));
        dadosCliente.put(ColunasPedido.LABEL_TELEFONE_FIXO, FormatacaoUtil.formatarTelefoneFixo(pedido.getTelefoneFixo()));
        return dadosCliente;
    }

    public static Map<String, String> montarEnderecoObra(PedidoDTO pedido, ConsultaLocalidadesService localidadeService) {
        Map<String, String> enderecoObra = new LinkedHashMap<>();
        enderecoObra.put("Estado", localidadeService.resolverEstado(pedido.getEstadoId()));
        enderecoObra.put("Município", localidadeService.resolverMunicipio(pedido.getMunicipioId()));
        enderecoObra.put("Bairro", localidadeService.resolverBairro(pedido.getBairroId()));
        enderecoObra.put("CEP", FormatacaoUtil.formatarCep(pedido.getCep()));
        enderecoObra.put("Referência", FormatacaoUtil.formatarTexto(pedido.getReferencia()));
        enderecoObra.put(ColunasPedido.LABEL_NUMERO, FormatacaoUtil.formatarTexto(pedido.getNumero()));
        return enderecoObra;
    }

    public static Map<String, String> montarEnderecoCliente(PedidoDTO pedido, ConsultaLocalidadesService localidadeService) {
        Map<String, String> enderecoCliente = new LinkedHashMap<>();
        enderecoCliente.put("Estado", localidadeService.resolverEstado(pedido.getEstadoClienteId()));
        enderecoCliente.put("Município", localidadeService.resolverMunicipio(pedido.getMunicipioClienteId()));
        enderecoCliente.put("Bairro", localidadeService.resolverBairro(pedido.getBairroClienteId()));
        enderecoCliente.put("CEP", FormatacaoUtil.formatarCep(pedido.getCepCliente()));
        enderecoCliente.put("Referência", FormatacaoUtil.formatarTexto(pedido.getReferenciaCliente()));
        enderecoCliente.put(ColunasPedido.LABEL_NUMERO_CLIENTE, FormatacaoUtil.formatarTexto(pedido.getNumeroCliente()));
        return enderecoCliente;
    }
}