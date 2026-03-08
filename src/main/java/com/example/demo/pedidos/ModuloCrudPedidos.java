package com.example.demo.pedidos;

import com.example.demo.shared.crud.ModuloCrud;
import com.example.demo.shared.crud.ProvedorModuloCrud;
import com.example.demo.shared.crud.filtros.FiltrosCrud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.shared.crud.ColunaCrud.col;

@Component
public class ModuloCrudPedidos implements ProvedorModuloCrud {

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
        return new ModuloCrud(
                chave(),
                "Pedidos",
                "/crud/" + chave(),
                List.of(
                        FiltrosCrud.text("busca", "Busca", ""),
                        FiltrosCrud.text("numero_busca", "Numero do pedido", "")
                ),
                List.of(
                        col("ID", ColunaPedido.ID_PEDIDO.getColuna()),
                        col("Número do Pedido", ColunaPedido.NUMERO_PEDIDO.getColuna()),
                        col("Cliente Nome", ColunaPedido.CLIENTE_NOME.getColuna()),
                        col("Email", ColunaPedido.EMAIL.getColuna()),
                        col("CPF", ColunaPedido.CPF.getColuna()),
                        col("RG", ColunaPedido.RG.getColuna()),
                        col("CNPJ", ColunaPedido.CNPJ.getColuna()),
                        col("Serviço Social", ColunaPedido.SERVICO_SOCIAL.getColuna()),
                        col("Profissão", ColunaPedido.PROFISSAO.getColuna()),
                        col("Adm. Obra", ColunaPedido.ADM_OBRA.getColuna()),
                        col("Telefone", ColunaPedido.TELEFONE.getColuna()),
                        col("Telefone Fixo", ColunaPedido.TELEFONE_FIXO.getColuna()),
                        col("Descrição", ColunaPedido.DESCRICAO.getColuna()),
                        col("Acabamento", ColunaPedido.ACABAMENTO.getColuna()),
                        col("Tubos", ColunaPedido.TUBOS.getColuna()),
                        col("Revestimento", ColunaPedido.REVESTIMENTO.getColuna()),
                        col("Valor Total", ColunaPedido.VALOR_TOTAL.getColuna()),
                        col("Prazo Montagem", ColunaPedido.PRAZO_MONTAGEM.getColuna()),
                        col("Número", ColunaPedido.NUMERO.getColuna()),
                        col("Bairro", ColunaPedido.BAIRRO.getColuna()),
                        col("Cidade", ColunaPedido.CIDADE.getColuna()),
                        col("CEP", ColunaPedido.CEP.getColuna()),
                        col("Referência", ColunaPedido.REFERENCIA.getColuna()),
                        col("Número Cliente", ColunaPedido.NUMERO_CLIENTE.getColuna()),
                        col("Bairro Cliente", ColunaPedido.BAIRRO_CLIENTE.getColuna()),
                        col("Cidade Cliente", ColunaPedido.CIDADE_CLIENTE.getColuna()),
                        col("CEP Cliente", ColunaPedido.CEP_CLIENTE.getColuna()),
                        col("Referência Cliente", ColunaPedido.REFERENCIA_CLIENTE.getColuna()),
                        col("Cliente", ColunaPedido.CLIENTE.getColuna()),
                        col("Valor", ColunaPedido.VALOR.getColuna())
                )
        );
    }

    @Override
    public List<Map<String, Object>> linhas(Map<String, String> parametros) {
        String busca = parametros.getOrDefault("busca", "").trim();
        String numeroBusca = parametros.getOrDefault("numero_busca", "").trim();

        int page = Integer.parseInt(parametros.getOrDefault("page", "0"));
        int size = Integer.parseInt(parametros.getOrDefault("size", "10"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Pedido> pedidos =
                pedidoRepository.findAll(
                        EspecificacaoPedido.filtro(busca, numeroBusca),
                        pageable
                );

        return pedidos.getContent().stream()
                .map(p -> {
                    Map<String, Object> row = new HashMap<>();

                    for (ColunaPedido col : ColunaPedido.values()) {
                        row.put(col.getColuna(), col.extract(p));
                    }

                    return row;
                })
                .toList();
    }
}
