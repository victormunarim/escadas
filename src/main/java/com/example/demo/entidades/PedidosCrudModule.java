package com.example.demo.entidades;

import com.example.demo.ENUM.PedidoColuna;
import com.example.demo.crud.CrudModule;
import com.example.demo.crud.CrudModuleProvider;
import com.example.demo.crud.filtros.Filters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.crud.CrudColumn.col;

@Component
public class PedidosCrudModule implements CrudModuleProvider {

    private final PedidoRepository pedidoRepository;

    public PedidosCrudModule(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public String key() {
        return "pedidos";
    }


    @Override
    public CrudModule module() {
        return new CrudModule(
                key(),
                "Pedidos",
                "/crud/" + key(),
                List.of(
                        Filters.text("busca", "Busca", ""),
                        Filters.text("numero_busca", "Numero do pedido", "")
                ),
                List.of(
                        col("ID", PedidoColuna.ID_PEDIDO.getColuna()),
                        col("Número do Pedido", PedidoColuna.NUMERO_PEDIDO.getColuna()),
                        col("Cliente Nome", PedidoColuna.CLIENTE_NOME.getColuna()),
                        col("Email", PedidoColuna.EMAIL.getColuna()),
                        col("CPF", PedidoColuna.CPF.getColuna()),
                        col("RG", PedidoColuna.RG.getColuna()),
                        col("CNPJ", PedidoColuna.CNPJ.getColuna()),
                        col("Serviço Social", PedidoColuna.SERVICO_SOCIAL.getColuna()),
                        col("Profissão", PedidoColuna.PROFISSAO.getColuna()),
                        col("Adm. Obra", PedidoColuna.ADM_OBRA.getColuna()),
                        col("Telefone", PedidoColuna.TELEFONE.getColuna()),
                        col("Telefone Fixo", PedidoColuna.TELEFONE_FIXO.getColuna()),
                        col("Descrição", PedidoColuna.DESCRICAO.getColuna()),
                        col("Acabamento", PedidoColuna.ACABAMENTO.getColuna()),
                        col("Tubos", PedidoColuna.TUBOS.getColuna()),
                        col("Revestimento", PedidoColuna.REVESTIMENTO.getColuna()),
                        col("Valor Total", PedidoColuna.VALOR_TOTAL.getColuna()),
                        col("Prazo Montagem", PedidoColuna.PRAZO_MONTAGEM.getColuna()),
                        col("Número", PedidoColuna.NUMERO.getColuna()),
                        col("Bairro", PedidoColuna.BAIRRO.getColuna()),
                        col("Cidade", PedidoColuna.CIDADE.getColuna()),
                        col("CEP", PedidoColuna.CEP.getColuna()),
                        col("Referência", PedidoColuna.REFERENCIA.getColuna()),
                        col("Número Cliente", PedidoColuna.NUMERO_CLIENTE.getColuna()),
                        col("Bairro Cliente", PedidoColuna.BAIRRO_CLIENTE.getColuna()),
                        col("Cidade Cliente", PedidoColuna.CIDADE_CLIENTE.getColuna()),
                        col("CEP Cliente", PedidoColuna.CEP_CLIENTE.getColuna()),
                        col("Referência Cliente", PedidoColuna.REFERENCIA_CLIENTE.getColuna()),
                        col("Flag Oculto", PedidoColuna.FLAG_OCULTO.getColuna()),
                        col("Cliente", PedidoColuna.CLIENTE.getColuna()),
                        col("Valor", PedidoColuna.VALOR.getColuna())
                )
        );
    }

    @Override
    public List<Map<String, Object>> rows(Map<String, String> params) {
        String busca = params.getOrDefault("busca", "").trim();
        String numeroBusca = params.getOrDefault("numero_busca", "").trim();

        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", "10"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Pedido> pedidos =
                pedidoRepository.findAll(
                        PedidoSpecification.filtro(busca, numeroBusca),
                        pageable
                );

        return pedidos.getContent().stream()
                .map(p -> {
                    Map<String, Object> row = new HashMap<>();

                    for (PedidoColuna col : PedidoColuna.values()) {
                        row.put(col.getColuna(), col.extract(p));
                    }

                    return row;
                })
                .toList();
    }
}