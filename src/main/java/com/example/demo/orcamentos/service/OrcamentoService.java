package com.example.demo.orcamentos.service;

import com.example.demo.orcamentos.config.FormularioOrcamentoViewConfig;
import com.example.demo.orcamentos.config.ListagemOrcamentosViewConfig;
import com.example.demo.orcamentos.dto.FormularioOrcamentoDTO;
import com.example.demo.orcamentos.dto.OrcamentoDTO;
import com.example.demo.orcamentos.model.EtiquetaEntity;
import com.example.demo.orcamentos.model.OrcamentoEntity;
import com.example.demo.orcamentos.repository.EtiquetaRepository;
import com.example.demo.orcamentos.repository.OrcamentoRepository;
import com.example.demo.orcamentos.spec.EspecificacaoOrcamento;
import com.example.demo.pedidos.model.PedidoEntity;
import com.example.demo.pedidos.repository.PedidoRepository;
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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrcamentoService implements CrudService<FormularioOrcamentoDTO> {

    private final OrcamentoRepository repositorioOrcamento;
    private final EtiquetaRepository repositorioEtiqueta;
    private final PedidoRepository repositorioPedido;
    private final ArquivoService servicoArquivo;

    public OrcamentoService(
            OrcamentoRepository repositorioOrcamento,
            EtiquetaRepository repositorioEtiqueta,
            PedidoRepository repositorioPedido,
            ArquivoService servicoArquivo
    ) {
        this.repositorioOrcamento = repositorioOrcamento;
        this.repositorioEtiqueta = repositorioEtiqueta;
        this.repositorioPedido = repositorioPedido;
        this.servicoArquivo = servicoArquivo;
    }

    @Override
    public ListagemDTO listarResumo(Map<String, String> parametros) {
        String busca = parametros.getOrDefault("busca", "").trim();
        String dia = parametros.getOrDefault("dia", "").trim();
        String mes = parametros.getOrDefault("mes", "").trim();
        String ano = parametros.getOrDefault("ano", "").trim();
        String tecnico = parametros.getOrDefault("tecnico", "false").trim();
        boolean ehTecnico = "true".equalsIgnoreCase(tecnico);
        String defaultEncerrado = ehTecnico ? "" : "false";
        String encerrado = parametros.getOrDefault("encerrado", defaultEncerrado).trim();
        String etiquetaId = parametros.getOrDefault("etiquetaId", "").trim();

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
        if (!parametros.containsKey("encerrado")) {
            parametros.put("encerrado", defaultEncerrado);
        }
        if (!parametros.containsKey("etiquetaId")) {
            parametros.put("etiquetaId", "");
        }
        if (!parametros.containsKey("tecnico")) {
            parametros.put("tecnico", "false");
        }

        int size = FormOptionsProvider.tamanhoPagina(parametros.getOrDefault("size", "50"));

        List<OrcamentoDTO> orcamentos = repositorioOrcamento.findAll(
                EspecificacaoOrcamento.filtro(busca, dia, mes, ano, encerrado, etiquetaId, tecnico),
                PageRequest.of(0, size)
        ).stream()
                .map(OrcamentoDTO::new)
                .sorted(Comparator.comparingInt((OrcamentoDTO o) -> obterPrioridadeEtiqueta(o.getEtiquetaNome()))
                        .thenComparing(OrcamentoDTO::getId, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        List<ColunaConfig<OrcamentoDTO>> configColunas = ListagemOrcamentosViewConfig.obterConfiguracaoColunas();

        List<ColunaListagem> colunas = configColunas.stream()
                .map(ColunaConfig::toColunaListagem)
                .toList();

        List<LinhaListagem> linhas = new ArrayList<>();
        for (OrcamentoDTO o : orcamentos) {
            Map<String, Object> valores = new LinkedHashMap<>();
            for (ColunaConfig<OrcamentoDTO> col : configColunas) {
                valores.put(col.chave(), col.extrairValor(o));
            }
            linhas.add(new LinhaListagem(o.getId(), valores));
        }

        List<CampoRender> filtros = new ArrayList<>();
        filtros.add(
                new CampoTextoRender(
                        "Busca",
                        "busca",
                        "filtro-crud",
                        "search",
                        false,
                        null,
                        null,
                        "Nome, bairro ou descrição...",
                        parametros.getOrDefault("busca", "")
                )
        );
        filtros.add(
                new CampoSelecaoRender(
                        "Etiqueta",
                        "etiquetaId",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("etiquetaId", ""),
                        criarOpcoesEtiquetas(true)
                )
        );
        if (!ehTecnico) {
            filtros.add(
                    new CampoSelecaoRender(
                            "Encerrado",
                            "encerrado",
                            "filtro-crud",
                            false,
                            parametros.getOrDefault("encerrado", "false"),
                            List.of(
                                    new OpcaoCrud("", "Selecione"),
                                    new OpcaoCrud("true", "Sim"),
                                    new OpcaoCrud("false", "Não")
                            )
                    )
            );
        }
        filtros.add(
                new CampoSelecaoRender(
                        "Dia",
                        "dia",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("dia", ""),
                        FormOptionsProvider.criarOpcoesDias()
                )
        );
        filtros.add(
                new CampoSelecaoRender(
                        "Mês",
                        "mes",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("mes", ""),
                        FormOptionsProvider.criarOpcoesMeses()
                )
        );
        filtros.add(
                new CampoSelecaoRender(
                        "Ano",
                        "ano",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("ano", ""),
                        FormOptionsProvider.criarOpcoesAnos()
                )
        );
        filtros.add(
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

    @Override
    @Transactional
    public void excluir(Long id) {
        OrcamentoEntity orcamento = obterOrcamento(id);
        orcamento.setFlagOculto(Boolean.TRUE);
        repositorioOrcamento.save(orcamento);
    }

    public OrcamentoDTO buscarPorId(Long id) {
        return new OrcamentoDTO(obterOrcamento(id));
    }

    @Override
    @Transactional
    public Long salvarFormulario(FormularioOrcamentoDTO formulario) {
        OrcamentoEntity orcamento = new OrcamentoEntity();
        aplicarFormulario(formulario, orcamento);
        orcamento.setFlagOculto(Boolean.FALSE);
        OrcamentoEntity salvo = repositorioOrcamento.save(orcamento);
        return salvo.getId();
    }

    @Override
    @Transactional
    public void atualizarFormulario(Long id, FormularioOrcamentoDTO formulario) {
        OrcamentoEntity orcamento = obterOrcamento(id);
        aplicarFormulario(formulario, orcamento);
        repositorioOrcamento.save(orcamento);
    }

    private void aplicarFormulario(FormularioOrcamentoDTO formulario, OrcamentoEntity orcamento) {
        orcamento.setNome(formulario.getNome());
        orcamento.setBairro(formulario.getBairro());
        orcamento.setDescricao(formulario.getDescricao());

        if (formulario.getEtiquetaId() == null) {
            throw new IllegalArgumentException("A etiqueta é obrigatória.");
        }

        EtiquetaEntity novaEtiqueta = repositorioEtiqueta.findById(formulario.getEtiquetaId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Etiqueta não encontrada com o ID: " + formulario.getEtiquetaId()
                ));

        orcamento.atualizarEtiqueta(novaEtiqueta);

        if (formulario.getPedidoId() != null) {
            PedidoEntity pedido = repositorioPedido.findById(Long.valueOf(formulario.getPedidoId()))
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Pedido não encontrado com o ID: " + formulario.getPedidoId()
                    ));
            orcamento.vincularPedido(pedido);
        } else {
            orcamento.vincularPedido(null);
        }
    }

    public FormularioOrcamentoDTO criarFormulario(Long id) {
        OrcamentoEntity orcamento = obterOrcamento(id);
        FormularioOrcamentoDTO form = new FormularioOrcamentoDTO();
        form.setNome(orcamento.getNome());
        form.setBairro(orcamento.getBairro());
        form.setDescricao(orcamento.getDescricao());
        if (orcamento.getEtiqueta() != null) {
            form.setEtiquetaId(orcamento.getEtiqueta().getId());
        }
        if (orcamento.getPedido() != null) {
            form.setPedidoId(orcamento.getPedido().getId());
        }
        return form;
    }

    public List<ArquivoDTO> listarArquivos(Long id) {
        return servicoArquivo.listar("orcamento_id", id);
    }

    @Transactional
    public void enviarArquivo(Long id, MultipartFile arquivo) {
        enviarArquivo(id, arquivo, 1);
    }

    @Transactional
    public void enviarArquivo(Long id, MultipartFile arquivo, Integer etapa) {
        OrcamentoEntity orcamento = obterOrcamento(id);
        String nomePasta = FormatacaoUtil.nomePastaOrcamento(orcamento.getNome(), id);
        servicoArquivo.enviarERegistrar("orcamento_id", id, nomePasta, arquivo, etapa);
    }

    @Transactional
    public void excluirArquivo(Long arquivoId) {
        servicoArquivo.excluir(arquivoId);
    }

    @Transactional
    public void encerrarOrcamento(Long id) {
        OrcamentoEntity orcamento = obterOrcamento(id);
        orcamento.encerrar();
        repositorioOrcamento.save(orcamento);
    }

    @Transactional
    public void reabrirOrcamento(Long id) {
        OrcamentoEntity orcamento = obterOrcamento(id);
        orcamento.reabrir();
        repositorioOrcamento.save(orcamento);
    }

    @Override
    public List<CampoRender> obterCamposRenderNovo() {
        return FormularioOrcamentoViewConfig.criarCampos(criarOpcoesEtiquetas(false), criarOpcoesPedidos(null)).stream()
                .map(base -> base.renderizar(new FormularioOrcamentoDTO()))
                .toList();
    }

    @Override
    public List<CampoRender> obterCamposRenderEdicao(Long id) {
        FormularioOrcamentoDTO form = criarFormulario(id);
        return FormularioOrcamentoViewConfig.criarCampos(criarOpcoesEtiquetas(false), criarOpcoesPedidos(id)).stream()
                .map(base -> base.renderizar(form))
                .toList();
    }

    private List<OpcaoCrud> criarOpcoesEtiquetas(boolean incluirSelecione) {
        List<OpcaoCrud> opcoes = new ArrayList<>();
        if (incluirSelecione) {
            opcoes.add(new OpcaoCrud("", "Selecione"));
        }
        List<EtiquetaEntity> etiquetas = new ArrayList<>(repositorioEtiqueta.findAll());
        etiquetas.sort(Comparator.comparingInt(e -> obterPrioridadeEtiqueta(e.getNome())));
        for (EtiquetaEntity e : etiquetas) {
            opcoes.add(new OpcaoCrud(String.valueOf(e.getId()), e.getNome()));
        }
        return opcoes;
    }

    private List<OpcaoCrud> criarOpcoesPedidos(Long orcamentoIdAtual) {
        List<OpcaoCrud> opcoes = new ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Nenhum"));

        Set<Integer> pedidoIdsComOrcamento = repositorioOrcamento.findAll().stream()
                .filter(o -> (o.getFlagOculto() == null || !o.getFlagOculto()))
                .filter(OrcamentoEntity::ehTecnico)
                .filter(o -> !o.getId().equals(orcamentoIdAtual))
                .map(o -> o.getPedido().getId())
                .collect(Collectors.toSet());

        repositorioPedido.findAll().stream()
                .filter(p -> p.getFlagOculto() == null || !p.getFlagOculto())
                .filter(p -> !pedidoIdsComOrcamento.contains(p.getId()))
                .forEach(p -> {
                    String clienteStr = p.getNomeCliente() != null && !p.getNomeCliente().isBlank()
                            ? " - " + p.getNomeCliente() : "";
                    String label = "Pedido #" + p.getNumeroPedido() + clienteStr;
                    opcoes.add(new OpcaoCrud(String.valueOf(p.getId()), label));
                });
        return opcoes;
    }

    private static int obterPrioridadeEtiqueta(String nomeEtiqueta) {
        if (nomeEtiqueta == null) return 5;
        return switch (nomeEtiqueta.trim().toLowerCase()) {
            case "riscar" -> 1;
            case "aguardando aprovação", "aguardando aprovacao" -> 2;
            case "3d / renderizar", "3d/renderizar" -> 3;
            case "proposta" -> 4;
            default -> 5;
        };
    }

    private OrcamentoEntity obterOrcamento(Long id) {
        return repositorioOrcamento.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado com o ID: " + id));
    }
}
