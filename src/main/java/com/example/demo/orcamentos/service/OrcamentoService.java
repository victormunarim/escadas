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

@Service
@Transactional(readOnly = true)
public class OrcamentoService implements CrudService<FormularioOrcamentoDTO> {

    private final OrcamentoRepository repositorioOrcamento;
    private final EtiquetaRepository repositorioEtiqueta;
    private final ArquivoService servicoArquivo;

    public OrcamentoService(
            OrcamentoRepository repositorioOrcamento,
            EtiquetaRepository repositorioEtiqueta,
            ArquivoService servicoArquivo
    ) {
        this.repositorioOrcamento = repositorioOrcamento;
        this.repositorioEtiqueta = repositorioEtiqueta;
        this.servicoArquivo = servicoArquivo;
    }

    @Override
    public ListagemDTO listarResumo(Map<String, String> parametros) {
        String busca = parametros.getOrDefault("busca", "").trim();
        String dia = parametros.getOrDefault("dia", "").trim();
        String mes = parametros.getOrDefault("mes", "").trim();
        String ano = parametros.getOrDefault("ano", "").trim();
        String encerrado = parametros.getOrDefault("encerrado", "false").trim();
        String etiquetaId = parametros.getOrDefault("etiquetaId", "").trim();

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
        if (!parametros.containsKey("encerrado")) {
            parametros.put("encerrado", "false");
        }
        if (!parametros.containsKey("etiquetaId")) {
            parametros.put("etiquetaId", "");
        }

        int size = tamanhoPagina(parametros.getOrDefault("size", "50"));

        List<OrcamentoDTO> orcamentos = repositorioOrcamento.findAll(
                EspecificacaoOrcamento.filtro(busca, dia, mes, ano, encerrado, etiquetaId),
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

        List<CampoRender> filtros = List.of(
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
                ),
                new CampoSelecaoRender(
                        "Etiqueta",
                        "etiquetaId",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("etiquetaId", ""),
                        criarOpcoesEtiquetas()
                ),
                new CampoSelecaoRender(
                        "Encerrado",
                        "encerrado",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("encerrado", ""),
                        List.of(
                                new OpcaoCrud("", "Selecione"),
                                new OpcaoCrud("true", "Sim"),
                                new OpcaoCrud("false", "Não")
                        )
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
        orcamento.setNome(formulario.getNome());
        orcamento.setBairro(formulario.getBairro());
        orcamento.setDescricao(formulario.getDescricao());
        orcamento.setFlagOculto(Boolean.FALSE);
        if (formulario.getEtiquetaId() != null) {
            orcamento.setEtiqueta(repositorioEtiqueta.findById(formulario.getEtiquetaId()).orElse(null));
        }
        OrcamentoEntity salvo = repositorioOrcamento.save(orcamento);
        return salvo.getId();
    }

    @Override
    @Transactional
    public void atualizarFormulario(Long id, FormularioOrcamentoDTO formulario) {
        OrcamentoEntity orcamento = obterOrcamento(id);
        orcamento.setNome(formulario.getNome());
        orcamento.setBairro(formulario.getBairro());
        orcamento.setDescricao(formulario.getDescricao());
        if (formulario.getEtiquetaId() != null) {
            orcamento.setEtiqueta(repositorioEtiqueta.findById(formulario.getEtiquetaId()).orElse(null));
        } else {
            orcamento.setEtiqueta(null);
        }
        repositorioOrcamento.save(orcamento);
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
        repositorioOrcamento.save(orcamento);
    }

    @Override
    public List<CampoRender> obterCamposRenderNovo() {
        return FormularioOrcamentoViewConfig.criarCampos(criarOpcoesEtiquetas()).stream()
                .map(base -> base.renderizar(new FormularioOrcamentoDTO()))
                .toList();
    }

    @Override
    public List<CampoRender> obterCamposRenderEdicao(Long id) {
        FormularioOrcamentoDTO form = criarFormulario(id);
        return FormularioOrcamentoViewConfig.criarCampos(criarOpcoesEtiquetas()).stream()
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
