package com.example.demo.orcamentos.service;

import com.example.demo.orcamentos.model.OrcamentoEntity;
import com.example.demo.orcamentos.repository.OrcamentoRepository;
import com.example.demo.orcamentos.repository.EtiquetaRepository;
import com.example.demo.orcamentos.dto.*;
import com.example.demo.orcamentos.spec.EspecificacaoOrcamento;
import com.example.demo.orcamentos.config.ListagemOrcamentosViewConfig;
import com.example.demo.orcamentos.config.FormularioOrcamentoViewConfig;
import com.example.demo.shared.crud.service.CrudService;
import com.example.demo.shared.crud.listagem.ColunaConfig;
import com.example.demo.shared.crud.render.*;
import com.example.demo.shared.crud.OpcaoCrud;
import com.example.demo.shared.arquivos.dto.ArquivoDTO;
import com.example.demo.shared.arquivos.service.ArquivoService;
import com.example.demo.shared.util.FormatacaoUtil;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.pedidos.repository.PedidoRepository;
import com.example.demo.pedidos.model.PedidoEntity;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        int size = tamanhoPagina(parametros.getOrDefault("size", "50"));

        List<OrcamentoDTO> orcamentos = repositorioOrcamento.findAll(
                EspecificacaoOrcamento.filtro(busca, dia, mes, ano, encerrado, etiquetaId, tecnico),
                PageRequest.of(0, size)
        ).stream()
                .map(OrcamentoDTO::new)
                .sorted(java.util.Comparator.comparingInt((OrcamentoDTO o) -> obterPrioridadeEtiqueta(o.getEtiquetaNome()))
                        .thenComparing(OrcamentoDTO::getId, java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder())))
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
                        criarOpcoesEtiquetas()
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
                        criarOpcoesDias()
                )
        );
        filtros.add(
                new CampoSelecaoRender(
                        "Mês",
                        "mes",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("mes", ""),
                        criarOpcoesMeses()
                )
        );
        filtros.add(
                new CampoSelecaoRender(
                        "Ano",
                        "ano",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("ano", ""),
                        criarOpcoesAnos()
                )
        );
        filtros.add(
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

        boolean jaEraTecnico = orcamento.getPedido() != null;

        com.example.demo.orcamentos.model.EtiquetaEntity novaEtiqueta = null;
        if (formulario.getEtiquetaId() != null) {
            novaEtiqueta = repositorioEtiqueta.findById(formulario.getEtiquetaId()).orElse(null);
        }

        if (jaEraTecnico) {
            if (novaEtiqueta != null && orcamento.getEtiqueta() != null && !novaEtiqueta.getId().equals(orcamento.getEtiqueta().getId())) {
                throw new IllegalArgumentException("Não é possível alterar a etiqueta de um Técnico.");
            }
        } else {
            orcamento.setEtiqueta(novaEtiqueta);
        }

        if (formulario.getPedidoId() != null) {
            PedidoEntity pedido = repositorioPedido.findById(Long.valueOf(formulario.getPedidoId()))
                    .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com o ID: " + formulario.getPedidoId()));

            com.example.demo.orcamentos.model.EtiquetaEntity etiquetaVerificacao = orcamento.getEtiqueta();
            if (etiquetaVerificacao == null || !"proposta".equalsIgnoreCase(etiquetaVerificacao.getNome())) {
                throw new IllegalArgumentException("Não é possível associar um pedido a um orçamento que não tenha a etiqueta 'Proposta'.");
            }

            orcamento.setPedido(pedido);
            orcamento.setFlagEncerrado(Boolean.TRUE);
        } else {
            if (!jaEraTecnico) {
                orcamento.setPedido(null);
            }
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
        orcamento.setFlagEncerrado(Boolean.TRUE);
        repositorioOrcamento.save(orcamento);
    }

    @Transactional
    public void reabrirOrcamento(Long id) {
        OrcamentoEntity orcamento = obterOrcamento(id);
        orcamento.setFlagEncerrado(Boolean.FALSE);
        orcamento.setPedido(null);
        repositorioOrcamento.save(orcamento);
    }

    @Override
    public List<CampoRender> obterCamposRenderNovo() {
        return FormularioOrcamentoViewConfig.criarCampos(criarOpcoesEtiquetas(), criarOpcoesPedidos(null)).stream()
                .map(base -> base.renderizar(new FormularioOrcamentoDTO()))
                .toList();
    }

    @Override
    public List<CampoRender> obterCamposRenderEdicao(Long id) {
        FormularioOrcamentoDTO form = criarFormulario(id);
        return FormularioOrcamentoViewConfig.criarCampos(criarOpcoesEtiquetas(), criarOpcoesPedidos(id)).stream()
                .map(base -> base.renderizar(form))
                .toList();
    }

    private List<OpcaoCrud> criarOpcoesEtiquetas() {
        List<OpcaoCrud> opcoes = new ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Selecione"));
        List<com.example.demo.orcamentos.model.EtiquetaEntity> etiquetas = new ArrayList<>(repositorioEtiqueta.findAll());
        etiquetas.sort(java.util.Comparator.comparingInt(e -> obterPrioridadeEtiqueta(e.getNome())));
        for (com.example.demo.orcamentos.model.EtiquetaEntity e : etiquetas) {
            opcoes.add(new OpcaoCrud(String.valueOf(e.getId()), e.getNome()));
        }
        return opcoes;
    }

    private List<OpcaoCrud> criarOpcoesPedidos(Long orcamentoIdAtual) {
        List<OpcaoCrud> opcoes = new ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Nenhum"));

        java.util.Set<Integer> pedidoIdsComOrcamento = repositorioOrcamento.findAll().stream()
                .filter(o -> (o.getFlagOculto() == null || !o.getFlagOculto()))
                .filter(o -> o.getPedido() != null)
                .filter(o -> orcamentoIdAtual == null || !o.getId().equals(orcamentoIdAtual))
                .map(o -> o.getPedido().getId())
                .collect(java.util.stream.Collectors.toSet());

        repositorioPedido.findAll().stream()
                .filter(p -> p.getFlagOculto() == null || !p.getFlagOculto())
                .filter(p -> !pedidoIdsComOrcamento.contains(p.getId()))
                .forEach(p -> {
                    String label = "Pedido #" + p.getNumeroPedido() + (p.getNomeCliente() != null && !p.getNomeCliente().isBlank() ? " - " + p.getNomeCliente() : "");
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

    private List<OpcaoCrud> criarOpcoesDias() {
        List<OpcaoCrud> opcoes = new ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Selecione"));
        for (int i = 1; i <= 31; i++) {
            String val = String.valueOf(i);
            opcoes.add(new OpcaoCrud(val, val));
        }
        return opcoes;
    }

    private List<OpcaoCrud> criarOpcoesMeses() {
        return List.of(
                new OpcaoCrud("", "Selecione"),
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
        List<OpcaoCrud> opcoes = new ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Selecione"));
        for (int i = 2020; i <= 2030; i++) {
            String val = String.valueOf(i);
            opcoes.add(new OpcaoCrud(val, val));
        }
        return opcoes;
    }
}
