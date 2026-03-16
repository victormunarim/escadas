package com.example.demo.service;

import com.example.demo.dto.ArquivoPedidoDTO;
import com.example.demo.dto.FormularioPedidoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.dto.PedidoResumoDTO;
import com.example.demo.constants.ColunasPedido;
import com.example.demo.entity.PedidoEntity;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.spec.EspecificacaoPedido;
import com.example.demo.util.FormatacaoUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    private final PedidoRepository repositorioPedido;
    private final FormularioPedidoService formularioPedidoService;
    private final ArquivoPedidoService servicoArquivoPedido;
    private final GeradorPdfPedidoService geradorPdfPedido;

    private static final DateTimeFormatter FORMATO_DATA_CADASTRO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    public List<Map<String, Object>> listarResumo(Map<String, String> parametros) {
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

        List<PedidoResumoDTO> pedidos = repositorioPedido.buscarResumo(
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

    public void inserir(PedidoDTO pedidoDTO) {
        PedidoEntity pedidoEntity = new PedidoEntity(pedidoDTO);
        if (pedidoEntity.getDataCadastro() == null) {
            pedidoEntity.setDataCadastro(LocalDateTime.now());
        }
        pedidoEntity.setFlagOculto(Boolean.FALSE);

        repositorioPedido.save(pedidoEntity);
    }

    public PedidoDTO editar(PedidoDTO pedidoDTO) {
        PedidoEntity pedidoEntity = new PedidoEntity(pedidoDTO);
        PedidoEntity salvo = repositorioPedido.save(pedidoEntity);
        return new PedidoDTO(salvo);
    }

    public void excluir(Long id) {
        PedidoEntity pedido = repositorioPedido.findById(id).orElse(null);
        if (pedido == null) {
            return;
        }

        pedido.setFlagOculto(Boolean.TRUE);
        repositorioPedido.save(pedido);
    }

    public List<PedidoDTO> listarTodos() {
        return repositorioPedido.findAll().stream()
                .map(PedidoDTO::new)
                .toList();
    }

    public PedidoDTO buscarPorId(Long id) {
        return repositorioPedido.findById(id)
                .map(PedidoDTO::new)
                .orElse(null);
    }

    public PedidoEntity buscarPorIdEntity(Long id) {
        return repositorioPedido.findById(id).orElse(null);
    }

    public PedidoDTO salvarFormulario(FormularioPedidoDTO formularioPedido) {
        PedidoEntity pedido = new PedidoEntity();
        formularioPedidoService.aplicarFormularioNoPedido(formularioPedido, pedido);
        pedido.setFlagOculto(Boolean.FALSE);
        return new PedidoDTO(repositorioPedido.save(pedido));
    }

    public boolean atualizarFormulario(Long id, FormularioPedidoDTO formularioPedido) {
        PedidoEntity pedido = repositorioPedido.findById(id).orElse(null);
        if (pedido == null) {
            return false;
        }
        formularioPedidoService.aplicarFormularioNoPedido(formularioPedido, pedido);
        repositorioPedido.save(pedido);
        return true;
    }

    public FormularioPedidoDTO criarFormulario(Long id) {
        PedidoEntity pedido = repositorioPedido.findById(id).orElse(null);
        if (pedido == null) {
            return null;
        }
        return formularioPedidoService.criarFormularioDePedido(pedido);
    }

    public List<ArquivoPedidoDTO> listarArquivos(Long pedidoId) {
        return servicoArquivoPedido.listarPorPedido(pedidoId);
    }

    public ArquivoPedidoDTO enviarArquivo(Long pedidoId, MultipartFile arquivo) {
        PedidoEntity pedido = repositorioPedido.findById(pedidoId).orElse(null);
        if (pedido == null) {
            throw new IllegalStateException("Pedido não encontrado.");
        }
        return servicoArquivoPedido.enviarERegistrar(pedido, arquivo);
    }

    public byte[] gerarPdf(Long pedidoId) {
        PedidoEntity pedido = repositorioPedido.findById(pedidoId).orElse(null);
        if (pedido == null) {
            return null;
        }
        return geradorPdfPedido.gerarPdfPedido(pedido);
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
