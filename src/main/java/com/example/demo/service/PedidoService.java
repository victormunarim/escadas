package com.example.demo.service;

import com.example.demo.dto.ArquivoPedidoDTO;
import com.example.demo.dto.FormularioPedidoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.dto.PedidoResumoDTO;
import com.example.demo.entity.PedidoEntity;
import com.example.demo.exception.PedidoNaoEncontradoException;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.PedidoResumoRepository;
import com.example.demo.spec.EspecificacaoPedido;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    private final PedidoRepository repositorioPedido;
    private final PedidoResumoRepository repositorioPedidoResumo;
    private final FormularioPedidoService formularioPedidoService;
    private final ArquivoPedidoService servicoArquivoPedido;
    private final GeradorPdfPedidoService geradorPdfPedido;

    public PedidoService(
            PedidoRepository repositorioPedido,
            PedidoResumoRepository repositorioPedidoResumo,
            FormularioPedidoService formularioPedidoService,
            ArquivoPedidoService servicoArquivoPedido,
            GeradorPdfPedidoService geradorPdfPedido
    ) {
        this.repositorioPedido = repositorioPedido;
        this.repositorioPedidoResumo = repositorioPedidoResumo;
        this.formularioPedidoService = formularioPedidoService;
        this.servicoArquivoPedido = servicoArquivoPedido;
        this.geradorPdfPedido = geradorPdfPedido;
    }

    public List<PedidoResumoDTO> listarResumo(Map<String, String> parametros) {
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

        return repositorioPedidoResumo.findAll(
                EspecificacaoPedido.filtro(busca, numeroBusca, dia, mes, ano),
                PageRequest.of(0, size)
        ).stream()
                .map(PedidoResumoDTO::new)
                .toList();
    }


    public void excluir(Long id) {
        PedidoEntity pedido = obterPedido(id);
        pedido.setFlagOculto(Boolean.TRUE);
        repositorioPedido.save(pedido);
    }

    public PedidoDTO buscarPorId(Long id) {
        return new PedidoDTO(obterPedido(id));
    }

    public void salvarFormulario(FormularioPedidoDTO formularioPedido) {
        PedidoEntity pedido = new PedidoEntity();
        formularioPedidoService.aplicarFormularioNoPedido(formularioPedido, pedido);
        pedido.setFlagOculto(Boolean.FALSE);
        new PedidoDTO(repositorioPedido.save(pedido));
    }

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
}
