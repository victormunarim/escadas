package com.example.demo.pedidos.service;
import com.example.demo.pedidos.model.PedidoEntity;
import com.example.demo.pedidos.repository.PedidoRepository;
import com.example.demo.pedidos.dto.*;
import com.example.demo.shared.crud.render.*;
import com.example.demo.shared.crud.service.CrudService;
import com.example.demo.pedidos.spec.EspecificacaoPedido;
import com.example.demo.pedidos.config.ListagemPedidosViewConfig;
import com.example.demo.shared.crud.listagem.ColunaConfig;
import com.example.demo.pedidos.exception.PedidoNaoEncontradoException;
import com.example.demo.shared.crud.OpcaoCrud;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PedidoService implements CrudService<FormularioPedidoDTO> {

    private final PedidoRepository repositorioPedido;
    private final FormularioPedidoService formularioPedidoService;
    private final ArquivoPedidoService servicoArquivoPedido;
    private final GeradorPdfPedidoService geradorPdfPedido;

    public PedidoService(
            PedidoRepository repositorioPedido,
            FormularioPedidoService formularioPedidoService,
            ArquivoPedidoService servicoArquivoPedido,
            GeradorPdfPedidoService geradorPdfPedido
    ) {
        this.repositorioPedido = repositorioPedido;
        this.formularioPedidoService = formularioPedidoService;
        this.servicoArquivoPedido = servicoArquivoPedido;
        this.geradorPdfPedido = geradorPdfPedido;
    }

    public ListagemDTO listarResumo(Map<String, String> parametros) {
        String busca = parametros.getOrDefault("busca", "").trim();
        String numeroBusca = parametros.getOrDefault("numero_busca", "").trim();
        String dia = parametros.getOrDefault("dia", "").trim();
        String mes = parametros.getOrDefault("mes", "").trim();
        String ano = parametros.getOrDefault("ano", "").trim();

        LocalDate hoje = LocalDate.now();
        if (!parametros.containsKey("mes")) {
            mes = String.valueOf(MesFiltro(mes, hoje));
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

        List<PedidoDTO> pedidos = repositorioPedido.findAll(
                EspecificacaoPedido.filtro(busca, numeroBusca, dia, mes, ano),
                PageRequest.of(0, size)
        ).stream()
                .map(PedidoDTO::new)
                .toList();

        List<ColunaConfig<PedidoDTO>> configColunas = ListagemPedidosViewConfig.obterConfiguracaoColunas();

        List<ColunaListagem> colunas = configColunas.stream()
                .map(ColunaConfig::toColunaListagem)
                .toList();

        List<LinhaListagem> linhas = new java.util.ArrayList<>();
        for (PedidoDTO p : pedidos) {
            java.util.Map<String, Object> valores = new java.util.LinkedHashMap<>();
            for (ColunaConfig<PedidoDTO> col : configColunas) {
                valores.put(col.chave(), col.extrairValor(p));
            }
            linhas.add(new LinhaListagem(p.id(), valores));
        }

        List<CampoRender> filtros = List.of(
                new CampoTextoRender(
                        "Busca",
                        "busca",
                        "filtro-crud",
                        "search",
                        false,
                        null,
                        null,
                        "Nome, email ou descrição...",
                        parametros.getOrDefault("busca", "")
                ),
                new CampoTextoRender(
                        "Número do pedido",
                        "numero_busca",
                        "filtro-crud",
                        "search",
                        false,
                        null,
                        null,
                        "Número...",
                        parametros.getOrDefault("numero_busca", "")
                ),
                new CampoSelecaoRender(
                        "Dia",
                        "dia",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("dia", ""),
                        criarOpcoesDias()
                ),
                new CampoSelecaoRender(
                        "Mês",
                        "mes",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("mes", ""),
                        criarOpcoesMeses()
                ),
                new CampoSelecaoRender(
                        "Ano",
                        "ano",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("ano", ""),
                        criarOpcoesAnos()
                ),
                new CampoSelecaoRender(
                        "Quantidade",
                        "size",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("size", "50"),
                        List.of(
                                new OpcaoCrud("50", "50"),
                                new OpcaoCrud("100", "100"),
                                new OpcaoCrud("200", "200"),
                                new OpcaoCrud("500", "500")
                        )
                )
        );

        return new ListagemDTO(colunas, linhas, filtros);
    }

    private String MesFiltro(String mes, LocalDate hoje) {
        return (mes == null || mes.isBlank()) ? String.valueOf(hoje.getMonthValue()) : mes;
    }


    @Transactional
    public void excluir(Long id) {
        PedidoEntity pedido = obterPedido(id);
        pedido.setFlagOculto(Boolean.TRUE);
        repositorioPedido.save(pedido);
    }

    public PedidoDTO buscarPorId(Long id) {
        return new PedidoDTO(obterPedido(id));
    }

    @Transactional
    public void salvarFormulario(FormularioPedidoDTO formularioPedido) {
        PedidoEntity pedido = new PedidoEntity();
        formularioPedidoService.aplicarFormularioNoPedido(formularioPedido, pedido);
        pedido.setFlagOculto(Boolean.FALSE);
        new PedidoDTO(repositorioPedido.save(pedido));
    }

    @Transactional
    public void atualizarFormulario(Long id, FormularioPedidoDTO formularioPedido) {
        PedidoEntity pedido = obterPedido(id);
        formularioPedidoService.aplicarFormularioNoPedido(formularioPedido, pedido);
        repositorioPedido.save(pedido);
    }

    public FormularioPedidoDTO criarFormulario(Long id) {
        return formularioPedidoService.criarFormularioDePedido(obterPedido(id));
    }

    public List<ArquivoPedidoDTO> listarArquivos(Long pedidoId) {
        return servicoArquivoPedido.listarPorPedido(pedidoId);
    }

    @Transactional
    public void enviarArquivo(Long pedidoId, MultipartFile arquivo) {
        servicoArquivoPedido.enviarERegistrar(obterPedido(pedidoId), arquivo);
    }

    public byte[] gerarPdf(Long pedidoId) {
        return geradorPdfPedido.gerarPdfPedido(obterPedido(pedidoId));
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

    private PedidoEntity obterPedido(Long id) {
        return repositorioPedido.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));
    }

    @Override
    public List<CampoRender> obterCamposRenderNovo() {
        return obterCamposRenderNovo(new FormularioPedidoDTO());
    }

    public List<CampoRender> obterCamposRenderNovo(FormularioPedidoDTO form) {
        return formularioPedidoService.obterCamposRender(form);
    }

    @Override
    public List<CampoRender> obterCamposRenderEdicao(Long id) {
        FormularioPedidoDTO form = criarFormulario(id);
        return formularioPedidoService.obterCamposRender(form);
    }

    private List<OpcaoCrud> criarOpcoesDias() {
        List<OpcaoCrud> opcoes = new java.util.ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Todos"));
        for (int i = 1; i <= 31; i++) {
            String val = String.valueOf(i);
            opcoes.add(new OpcaoCrud(val, val));
        }
        return opcoes;
    }

    private List<OpcaoCrud> criarOpcoesMeses() {
        return List.of(
                new OpcaoCrud("", "Todos"),
                new OpcaoCrud("1", "Janeiro"),
                new OpcaoCrud("2", "Fevereiro"),
                new OpcaoCrud("3", "Março"),
                new OpcaoCrud("4", "Abril"),
                new OpcaoCrud("5", "Maio"),
                new OpcaoCrud("6", "Junho"),
                new OpcaoCrud("7", "Julho"),
                new OpcaoCrud("8", "Agosto"),
                new OpcaoCrud("9", "Setembro"),
                new OpcaoCrud("10", "Outubro"),
                new OpcaoCrud("11", "Novembro"),
                new OpcaoCrud("12", "Dezembro")
        );
    }

    private List<OpcaoCrud> criarOpcoesAnos() {
        List<OpcaoCrud> opcoes = new java.util.ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Todos"));
        for (int i = 2020; i <= 2030; i++) {
            String val = String.valueOf(i);
            opcoes.add(new OpcaoCrud(val, val));
        }
        return opcoes;
    }
}