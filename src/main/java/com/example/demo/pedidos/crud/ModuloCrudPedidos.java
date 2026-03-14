package com.example.demo.pedidos.crud;

import com.example.demo.pedidos.model.ColunasPedido;
import com.example.demo.pedidos.dto.PedidoResumo;
import com.example.demo.pedidos.repository.RepositorioPedido;
import com.example.demo.pedidos.spec.EspecificacaoPedido;
import com.example.demo.shared.crud.ModuloCrud;
import com.example.demo.shared.crud.OpcaoCrud;
import com.example.demo.shared.crud.ProvedorModuloCrud;
import com.example.demo.shared.crud.filtros.FiltrosCrudBuilder;
import com.example.demo.shared.crud.filtros.OpcoesDataCrud;
import com.example.demo.util.FormatacaoUtil;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.shared.crud.ColunaCrud.col;

@Component
public class ModuloCrudPedidos implements ProvedorModuloCrud {
    private static final DateTimeFormatter FORMATO_DATA_CADASTRO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final RepositorioPedido pedidoRepository;

    public ModuloCrudPedidos(RepositorioPedido pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public String chave() {
        return "pedidos";
    }

    @Override
    public ModuloCrud modulo() {
        List<OpcaoCrud> opcoesQuantidade = List.of(
                new OpcaoCrud("50", "50"),
                new OpcaoCrud("100", "100"),
                new OpcaoCrud("200", "200"),
                new OpcaoCrud("500", "500")
        );

        return new ModuloCrud(
                chave(),
                "Pedidos",
                "/crud/" + chave(),
                FiltrosCrudBuilder.criar()
                        .texto("busca", "Busca", "")
                        .texto("numero_busca", "Numero do pedido", "")
                        .selecao("dia", "Dia", OpcoesDataCrud.DIAS)
                        .selecao("mes", "Mês", OpcoesDataCrud.MESES)
                        .selecao("ano", "Ano", OpcoesDataCrud.ANOS)
                        .selecao("size", "Quantidade", opcoesQuantidade)
                        .build(),
                List.of(
                        col(ColunasPedido.LABEL_ID_PEDIDO, ColunasPedido.ID_PEDIDO),
                        col(ColunasPedido.LABEL_NUMERO_PEDIDO, ColunasPedido.NUMERO_PEDIDO),
                        col(ColunasPedido.LABEL_CLIENTE_NOME, ColunasPedido.CLIENTE_NOME),
                        col(ColunasPedido.LABEL_EMAIL, ColunasPedido.EMAIL),
                        col(ColunasPedido.LABEL_CPF, ColunasPedido.CPF),
                        col(ColunasPedido.LABEL_RG, ColunasPedido.RG),
                        col(ColunasPedido.LABEL_CNPJ, ColunasPedido.CNPJ),
                        col(ColunasPedido.LABEL_SERVICO_SOCIAL, ColunasPedido.SERVICO_SOCIAL),
                        col(ColunasPedido.LABEL_PROFISSAO, ColunasPedido.PROFISSAO),
                        col(ColunasPedido.LABEL_ADM_OBRA, ColunasPedido.ADM_OBRA),
                        col(ColunasPedido.LABEL_TELEFONE, ColunasPedido.TELEFONE),
                        col(ColunasPedido.LABEL_TELEFONE_FIXO, ColunasPedido.TELEFONE_FIXO),
                        col(ColunasPedido.LABEL_DESCRICAO, ColunasPedido.DESCRICAO),
                        col(ColunasPedido.LABEL_ACABAMENTO, ColunasPedido.ACABAMENTO),
                        col(ColunasPedido.LABEL_TUBOS, ColunasPedido.TUBOS),
                        col(ColunasPedido.LABEL_REVESTIMENTO, ColunasPedido.REVESTIMENTO),
                        col(ColunasPedido.LABEL_VALOR_TOTAL, ColunasPedido.VALOR_TOTAL),
                        col(ColunasPedido.LABEL_PRAZO_MONTAGEM, ColunasPedido.PRAZO_MONTAGEM),
                        col(ColunasPedido.LABEL_DATA_CADASTRO, ColunasPedido.DATA_CADASTRO),
                        col(ColunasPedido.LABEL_VALOR, ColunasPedido.VALOR)
                )
        );
    }

    @Override
    public List<Map<String, Object>> linhas(Map<String, String> parametros) {
        String busca = parametros.getOrDefault("busca", "").trim();
        String numeroBusca = parametros.getOrDefault("numero_busca", "").trim();
        String dia = parametros.getOrDefault("dia", "").trim();
        String mes = parametros.getOrDefault("mes", "").trim();
        String ano = parametros.getOrDefault("ano", "").trim();

        LocalDate hoje = LocalDate.now();
        if (!parametros.containsKey("mes")) {
            mes = String.valueOf(hoje.getMonthValue());
            parametros.put("mes", mes);
        }
        if (!parametros.containsKey("ano")) {
            ano = String.valueOf(hoje.getYear());
            parametros.put("ano", ano);
        }
        if (!parametros.containsKey("dia")) {
            parametros.put("dia", "");
        }

        int size = tamanhoPagina(parametros.getOrDefault("size", "50"));

        List<PedidoResumo> pedidos = pedidoRepository.buscarResumo(
                EspecificacaoPedido.filtro(busca, numeroBusca, dia, mes, ano),
                PageRequest.of(0, size)
        );

        return pedidos.stream()
                .map(p -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put(ColunasPedido.ID_PEDIDO, p.getId());
                    row.put(ColunasPedido.NUMERO_PEDIDO, p.getNumeroPedido());
                    row.put(ColunasPedido.CLIENTE_NOME, p.getNomeCliente());
                    row.put(ColunasPedido.EMAIL, p.getEmail());
                    row.put(ColunasPedido.CPF, FormatacaoUtil.formatarCpf(p.getCpf()));
                    row.put(ColunasPedido.RG, FormatacaoUtil.formatarRg(p.getRg()));
                    row.put(ColunasPedido.CNPJ, p.getCnpj());
                    row.put(ColunasPedido.SERVICO_SOCIAL, p.getServicoSocial());
                    row.put(ColunasPedido.PROFISSAO, p.getProfissao());
                    row.put(ColunasPedido.ADM_OBRA, p.getAdmObra());
                    row.put(ColunasPedido.TELEFONE, FormatacaoUtil.formatarTelefoneCelular(p.getTelefone()));
                    row.put(ColunasPedido.TELEFONE_FIXO, FormatacaoUtil.formatarTelefoneFixo(p.getTelefoneFixo()));
                    row.put(ColunasPedido.DESCRICAO, p.getDescricao());
                    row.put(ColunasPedido.ACABAMENTO, p.getAcabamento());
                    row.put(ColunasPedido.TUBOS, p.getTubos());
                    row.put(
                            ColunasPedido.REVESTIMENTO,
                            Boolean.TRUE.equals(p.getRevestimento()) ? "Sim" : "Não"
                    );
                    row.put(ColunasPedido.VALOR_TOTAL, FormatacaoUtil.formatarValor(p.getValorTotal()));
                    row.put(ColunasPedido.PRAZO_MONTAGEM, p.getPrazoMontagem());
                    row.put(ColunasPedido.NUMERO, p.getNumero());
                    row.put(ColunasPedido.BAIRRO, p.getBairro());
                    row.put(ColunasPedido.MUNICIPIO, p.getMunicipio());
                    row.put(ColunasPedido.CEP, FormatacaoUtil.formatarCep(p.getCep()));
                    row.put(ColunasPedido.REFERENCIA, p.getReferencia());
                    row.put(ColunasPedido.NUMERO_CLIENTE, p.getNumeroCliente());
                    row.put(ColunasPedido.BAIRRO_CLIENTE, p.getBairroCliente());
                    row.put(ColunasPedido.MUNICIPIO_CLIENTE, p.getMunicipioCliente());
                    row.put(ColunasPedido.CEP_CLIENTE, FormatacaoUtil.formatarCep(p.getCepCliente()));
                    row.put(ColunasPedido.REFERENCIA_CLIENTE, p.getReferenciaCliente());
                    row.put(
                            ColunasPedido.DATA_CADASTRO,
                            p.getDataCadastro() == null ? "" : p.getDataCadastro().format(FORMATO_DATA_CADASTRO)
                    );
                    row.put(ColunasPedido.FLAG_OCULTO, p.getFlagOculto());
                    row.put(ColunasPedido.VALOR, FormatacaoUtil.formatarValor(p.getValor()));

                    return row;
                })
                .toList();
    }

    private int tamanhoPagina(String valor) {
        if (valor == null || valor.isBlank()) {
            return 50;
        }
        return switch (valor.trim()) {
            case "50" -> 50;
            case "100" -> 100;
            case "200" -> 200;
            case "500" -> 500;
            default -> 50;
        };
    }
}
