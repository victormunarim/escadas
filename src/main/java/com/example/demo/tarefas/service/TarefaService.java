package com.example.demo.tarefas.service;

import com.example.demo.auth.model.UsuarioEntity;
import com.example.demo.auth.repository.UsuarioRepository;
import com.example.demo.tarefas.model.TarefaEntity;
import com.example.demo.tarefas.model.TipoTarefaEntity;
import com.example.demo.tarefas.repository.TarefaRepository;
import com.example.demo.tarefas.repository.TipoTarefaRepository;
import com.example.demo.tarefas.dto.*;
import com.example.demo.tarefas.spec.EspecificacaoTarefa;
import com.example.demo.tarefas.config.ListagemTarefasViewConfig;
import com.example.demo.tarefas.config.FormularioTarefaViewConfig;
import com.example.demo.shared.crud.service.CrudService;
import com.example.demo.shared.crud.listagem.ColunaConfig;
import com.example.demo.shared.crud.render.*;
import com.example.demo.shared.crud.OpcaoCrud;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TarefaService implements CrudService<FormularioTarefaDTO> {

    private final TarefaRepository repositorioTarefa;
    private final TipoTarefaRepository repositorioTipoTarefa;
    private final UsuarioRepository repositorioUsuario;

    public TarefaService(
            TarefaRepository repositorioTarefa,
            TipoTarefaRepository repositorioTipoTarefa,
            UsuarioRepository repositorioUsuario
    ) {
        this.repositorioTarefa = repositorioTarefa;
        this.repositorioTipoTarefa = repositorioTipoTarefa;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public ListagemDTO listarResumo(Map<String, String> parametros) {
        String busca = parametros.getOrDefault("busca", "").trim();
        String responsavel = parametros.getOrDefault("responsavel", "").trim();
        String concluida = parametros.getOrDefault("concluida", "false").trim();
        String dia = parametros.getOrDefault("dia", "").trim();
        String mes = parametros.getOrDefault("mes", "").trim();
        String ano = parametros.getOrDefault("ano", "").trim();

        LocalDate hoje = LocalDate.now();
        if (!parametros.containsKey("concluida")) {
            parametros.put("concluida", "false");
        }
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

        List<TarefaDTO> tarefas = repositorioTarefa.findAll(
                EspecificacaoTarefa.filtro(busca, responsavel, concluida, dia, mes, ano),
                PageRequest.of(0, size)
        ).stream()
                .map(TarefaDTO::new)
                .toList();

        List<ColunaConfig<TarefaDTO>> configColunas = ListagemTarefasViewConfig.obterConfiguracaoColunas();

        List<ColunaListagem> colunas = configColunas.stream()
                .map(ColunaConfig::toColunaListagem)
                .toList();

        List<LinhaListagem> linhas = new ArrayList<>();
        for (TarefaDTO t : tarefas) {
            Map<String, Object> valores = new LinkedHashMap<>();
            for (ColunaConfig<TarefaDTO> col : configColunas) {
                valores.put(col.chave(), col.extrairValor(t));
            }
            linhas.add(new LinhaListagem(t.getId(), valores));
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
                        "Tipo, responsável...",
                        parametros.getOrDefault("busca", "")
                ),
                new CampoSelecaoRender(
                        "Responsável",
                        "responsavel",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("responsavel", ""),
                        obterOpcoesResponsavelFiltro()
                ),
                new CampoSelecaoRender(
                        "Concluída",
                        "concluida",
                        "filtro-crud",
                        false,
                        parametros.getOrDefault("concluida", ""),
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
        TarefaEntity tarefa = obterTarefa(id);
        tarefa.setFlagOculto(Boolean.TRUE);
        repositorioTarefa.save(tarefa);
    }

    @Transactional
    public void concluirTarefa(Long id) {
        TarefaEntity tarefa = obterTarefa(id);
        tarefa.setFlagConcluida(Boolean.TRUE);
        repositorioTarefa.save(tarefa);
    }

    public List<TarefaDTO> listarPorReferencia(String extChave, Long extId) {
        return repositorioTarefa.findByExtChaveAndExtIdOrderByDataCadastroDesc(extChave, extId).stream()
                .map(TarefaDTO::new)
                .toList();
    }

    public TarefaDTO buscarPorId(Long id) {
        return new TarefaDTO(obterTarefa(id));
    }

    @Override
    @Transactional
    public Long salvarFormulario(FormularioTarefaDTO formulario) {
        TarefaEntity entity = new TarefaEntity();
        aplicarFormulario(formulario, entity);
        entity.setFlagOculto(Boolean.FALSE);
        entity.setFlagConcluida(Boolean.FALSE);
        TarefaEntity salvo = repositorioTarefa.save(entity);
        return salvo.getId();
    }

    @Override
    @Transactional
    public void atualizarFormulario(Long id, FormularioTarefaDTO formulario) {
        TarefaEntity entity = obterTarefa(id);
        aplicarFormulario(formulario, entity);
        repositorioTarefa.save(entity);
    }

    private void aplicarFormulario(FormularioTarefaDTO form, TarefaEntity entity) {
        TipoTarefaEntity tipo = repositorioTipoTarefa.findById(form.getTipoTarefaId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de tarefa não encontrado. ID: " + form.getTipoTarefaId()));
        UsuarioEntity responsavel = repositorioUsuario.findById(form.getResponsavelId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário responsável não encontrado. ID: " + form.getResponsavelId()));

        entity.setTipoTarefa(tipo);
        entity.setResponsavel(responsavel);
        entity.setDescricao(form.getDescricao() == null || form.getDescricao().isBlank() ? null : form.getDescricao().trim());
        if (form.getExtChave() != null && !form.getExtChave().isBlank()) {
            entity.setExtChave(form.getExtChave().trim());
        }
        if (form.getExtId() != null) {
            entity.setExtId(form.getExtId());
        }
    }

    public FormularioTarefaDTO criarFormulario(Long id) {
        TarefaEntity entity = obterTarefa(id);
        FormularioTarefaDTO form = new FormularioTarefaDTO();
        if (entity.getTipoTarefa() != null) {
            form.setTipoTarefaId(entity.getTipoTarefa().getId());
        }
        if (entity.getResponsavel() != null) {
            form.setResponsavelId(entity.getResponsavel().getId());
        }
        form.setFlagConcluida(entity.getFlagConcluida());
        form.setExtChave(entity.getExtChave());
        form.setExtId(entity.getExtId());
        form.setDescricao(entity.getDescricao());
        return form;
    }

    @Override
    public List<CampoRender> obterCamposRenderNovo() {
        return FormularioTarefaViewConfig.criarCampos(obterOpcoesTipoTarefa(), obterOpcoesResponsavel()).stream()
                .map(base -> base.renderizar(new FormularioTarefaDTO()))
                .toList();
    }

    @Override
    public List<CampoRender> obterCamposRenderEdicao(Long id) {
        FormularioTarefaDTO form = criarFormulario(id);
        return FormularioTarefaViewConfig.criarCampos(obterOpcoesTipoTarefa(), obterOpcoesResponsavel()).stream()
                .map(base -> base.renderizar(form))
                .toList();
    }

    private TarefaEntity obterTarefa(Long id) {
        return repositorioTarefa.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com o ID: " + id));
    }

    private List<OpcaoCrud> obterOpcoesTipoTarefa() {
        return repositorioTipoTarefa.findAll().stream()
                .map(t -> new OpcaoCrud(String.valueOf(t.getId()), t.getNome()))
                .collect(Collectors.toList());
    }

    private List<OpcaoCrud> obterOpcoesResponsavel() {
        return repositorioUsuario.findAll().stream()
                .map(u -> new OpcaoCrud(String.valueOf(u.getId()), u.getLogin()))
                .collect(Collectors.toList());
    }

    private List<OpcaoCrud> obterOpcoesResponsavelFiltro() {
        List<OpcaoCrud> opcoes = new ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Selecione"));
        opcoes.addAll(repositorioUsuario.findAll().stream()
                .map(u -> new OpcaoCrud(u.getLogin(), u.getLogin()))
                .toList());
        return opcoes;
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
