package com.example.demo.pedidos.service;

import com.example.demo.auth.repository.UsuarioRepository;
import com.example.demo.pedidos.config.ListagemPedidosViewConfig;
import com.example.demo.pedidos.dto.FormularioPedidoDTO;
import com.example.demo.pedidos.dto.PedidoDTO;
import com.example.demo.pedidos.exception.PedidoNaoEncontradoException;
import com.example.demo.pedidos.model.PedidoEntity;
import com.example.demo.pedidos.repository.PedidoRepository;
import com.example.demo.pedidos.spec.EspecificacaoPedido;
import com.example.demo.shared.arquivos.dto.ArquivoDTO;
import com.example.demo.shared.arquivos.service.ArquivoService;
import com.example.demo.shared.crud.OpcaoCrud;
import com.example.demo.shared.crud.listagem.ColunaConfig;
import com.example.demo.shared.crud.render.CampoRender;
import com.example.demo.shared.crud.render.CampoSelecaoRender;
import com.example.demo.shared.crud.render.CampoTextoRender;
import com.example.demo.shared.crud.render.ColunaListagem;
import com.example.demo.shared.crud.render.LinhaListagem;
import com.example.demo.shared.crud.render.ListagemDTO;
import com.example.demo.shared.crud.service.CrudService;
import com.example.demo.shared.crud.util.FormOptionsProvider;
import com.example.demo.shared.util.FormatacaoUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PedidoService implements CrudService<FormularioPedidoDTO> {

    private final PedidoRepository repositorioPedido;
    private final FormularioPedidoService formularioPedidoService;
    private final ArquivoService servicoArquivo;
    private final GeradorPdfPedidoService geradorPdfPedido;
    private final UsuarioRepository repositorioUsuario;

    public PedidoService(
            PedidoRepository repositorioPedido,
            FormularioPedidoService formularioPedidoService,
            ArquivoService servicoArquivo,
            GeradorPdfPedidoService geradorPdfPedido,
            UsuarioRepository repositorioUsuario
    ) {
        this.repositorioPedido = repositorioPedido;
        this.formularioPedidoService = formularioPedidoService;
        this.servicoArquivo = servicoArquivo;
        this.geradorPdfPedido = geradorPdfPedido;
        this.repositorioUsuario = repositorioUsuario;
    }

    public ListagemDTO listarResumo(Map<String, String> parametros) {
        String busca = parametros.getOrDefault("busca", "").trim();
        String numeroBusca = parametros.getOrDefault("numero_busca", "").trim();
        String dia = parametros.getOrDefault("dia", "").trim();
        String mes = parametros.getOrDefault("mes", "").trim();
        String ano = parametros.getOrDefault("ano", "").trim();
        String temOrcamento = parametros.getOrDefault("tem_orcamento", "false").trim();

        if (!parametros.containsKey("mes")) {
            parametros.put("mes", "");
            mes = "";
        }
        if (!parametros.containsKey("ano")) {
            parametros.put("ano", "");
            ano = "";
        }
        if (!parametros.containsKey("dia")) {
            parametros.put("dia", "");
        }
        if (!parametros.containsKey("tem_orcamento")) {
            parametros.put("tem_orcamento", "false");
        }

        int size = tamanhoPagina(parametros.getOrDefault("size", "50"));

        List<PedidoDTO> pedidos = repositorioPedido.findAll(
                EspecificacaoPedido.filtro(busca, numeroBusca, dia, mes, ano, temOrcamento),
                PageRequest.of(0, size)
        ).stream()
                .map(PedidoDTO::new)
                .toList();

        List<ColunaConfig<PedidoDTO>> configColunas = ListagemPedidosViewConfig.obterConfiguracaoColunas();

        List<ColunaListagem> colunas = configColunas.stream()
                .map(ColunaConfig::toColunaListagem)
                .toList();

        List<LinhaListagem> linhas = new ArrayList<>();
        for (PedidoDTO p : pedidos) {
            Map<String, Object> valores = new LinkedHashMap<>();
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
                        "Tem orçamento",
                        "tem_orcamento",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("tem_orcamento", "false"),
                        List.of(
                                new OpcaoCrud("false", "Não"),
                                new OpcaoCrud("true", "Sim"),
                                new OpcaoCrud("", "Selecione")
                        )
                ),
                new CampoSelecaoRender(
                        "Dia",
                        "dia",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("dia", ""),
                        FormOptionsProvider.criarOpcoesDias()
                ),
                new CampoSelecaoRender(
                        "Mês",
                        "mes",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("mes", ""),
                        FormOptionsProvider.criarOpcoesMeses()
                ),
                new CampoSelecaoRender(
                        "Ano",
                        "ano",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("ano", ""),
                        FormOptionsProvider.criarOpcoesAnos()
                ),
                new CampoSelecaoRender(
                        "Quantidade",
                        "size",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("size", "50"),
                        FormOptionsProvider.criarOpcoesTamanhoPagina()
                )
        );

        return new ListagemDTO(colunas, linhas, filtros);
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

    @Override
    @Transactional
    public Long salvarFormulario(FormularioPedidoDTO formularioPedido) {
        PedidoEntity pedido = new PedidoEntity();
        formularioPedidoService.aplicarFormularioNoPedido(formularioPedido, pedido);
        pedido.setFlagOculto(Boolean.FALSE);
        PedidoEntity pedidoSalvo = repositorioPedido.save(pedido);

        return Long.valueOf(pedidoSalvo.getId());
    }

    @Override
    @Transactional
    public void atualizarFormulario(Long id, FormularioPedidoDTO formularioPedido) {
        PedidoEntity pedido = obterPedido(id);
        formularioPedidoService.aplicarFormularioNoPedido(formularioPedido, pedido);
        repositorioPedido.save(pedido);
    }

    public FormularioPedidoDTO criarFormulario(Long id) {
        return formularioPedidoService.criarFormularioDePedido(obterPedido(id));
    }

    public List<ArquivoDTO> listarArquivos(Long pedidoId) {
        return servicoArquivo.listar("pedido_id", pedidoId);
    }

    @Transactional
    public void enviarArquivo(Long pedidoId, MultipartFile arquivo) {
        PedidoEntity pedido = obterPedido(pedidoId);
        String nomePasta = FormatacaoUtil.nomePastaPedido(pedido.getNomeCliente(), pedido.getNumeroPedido());
        servicoArquivo.enviarERegistrar("pedido_id", pedidoId, nomePasta, arquivo);
    }

    @Transactional
    public void excluirArquivo(Long arquivoId) {
        servicoArquivo.excluir(arquivoId);
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
}