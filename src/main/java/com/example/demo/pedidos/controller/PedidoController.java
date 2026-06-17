package com.example.demo.pedidos.controller;

import com.example.demo.pedidos.dto.PedidoDTO;
import com.example.demo.pedidos.dto.FormularioPedidoDTO;
import com.example.demo.pedidos.dto.ArquivoPedidoDTO;
import com.example.demo.pedidos.service.PedidoService;
import com.example.demo.pedidos.service.FormularioPedidoService;
import com.example.demo.pedidos.config.PedidoViewPresenter;
import com.example.demo.pedidos.exception.PedidoNaoEncontradoException;

import com.example.demo.shared.crud.OpcaoCrud;
import com.example.demo.shared.crud.filtros.OpcoesDataCrud;
import com.example.demo.localidades.service.ConsultaLocalidadesService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class PedidoController {
    private final PedidoService pedidoService;
    private final FormularioPedidoService formularioPedidoService;
    private final ConsultaLocalidadesService consultaLocalidadesService;

    public PedidoController(
            PedidoService pedidoService,
            FormularioPedidoService formularioPedidoService,
            ConsultaLocalidadesService consultaLocalidadesService
    ) {
        this.pedidoService = pedidoService;
        this.formularioPedidoService = formularioPedidoService;
        this.consultaLocalidadesService = consultaLocalidadesService;
    }

    @GetMapping("/pedidos")
    public String listarPedidos(
            @RequestParam Map<String, String> parametros,
            Model model
    ) {
        Map<String, String> parametrosEfetivos = new java.util.HashMap<>(parametros);
        parametrosEfetivos.putIfAbsent("size", "50");

        List<PedidoDTO> linhas = pedidoService.listarResumo(parametrosEfetivos);

        List<OpcaoCrud> opcoesQuantidade = List.of(
                new OpcaoCrud("50", "50"),
                new OpcaoCrud("100", "100"),
                new OpcaoCrud("200", "200"),
                new OpcaoCrud("500", "500")
        );

        model.addAttribute("parametros", parametrosEfetivos);
        model.addAttribute("linhas", linhas);
        model.addAttribute("opcoesDias", OpcoesDataCrud.DIAS);
        model.addAttribute("opcoesMeses", OpcoesDataCrud.MESES);
        model.addAttribute("opcoesAnos", OpcoesDataCrud.ANOS);
        model.addAttribute("opcoesQuantidade", opcoesQuantidade);

        return "index";
    }

    @GetMapping("/pedidos/{id}/visualizar")
    public String visualizar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        PedidoDTO pedido;
        try {
            pedido = pedidoService.buscarPorId(id);
        } catch (PedidoNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido não encontrado.");
            return "redirect:/pedidos";
        }

        List<ArquivoPedidoDTO> arquivosPedido = pedidoService.listarArquivos(id);

        model.addAttribute("pedidoId", pedido.getId());
        model.addAttribute("resumoPedido", PedidoViewPresenter.montarResumoPedido(pedido));
        model.addAttribute("dadosCliente", PedidoViewPresenter.montarDadosCliente(pedido));
        model.addAttribute("enderecoObra", PedidoViewPresenter.montarEnderecoObra(pedido, consultaLocalidadesService));
        model.addAttribute("enderecoCliente", PedidoViewPresenter.montarEnderecoCliente(pedido, consultaLocalidadesService));
        model.addAttribute("arquivosPedido", arquivosPedido);

        return "pedido-visualizacao";
    }

    @PostMapping("/pedidos/{id}/arquivos")
    public String enviarArquivoPedido(
            @PathVariable Long id,
            @RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirectAttributes
    ) {
        if (arquivo == null || arquivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Selecione um arquivo para enviar.");
            return "redirect:/pedidos/" + id + "/visualizar";
        }

        try {
            pedidoService.enviarArquivo(id, arquivo);
            redirectAttributes.addFlashAttribute("sucesso", "Arquivo enviado e vinculado ao pedido.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/pedidos/" + id + "/visualizar";
    }

    @GetMapping("/pedidos/novo")
    public String novoPedido(Model model) {
        formularioPedidoService.prepararPaginaFormulario(
                model,
                new FormularioPedidoDTO(),
                "/pedidos",
                "Novo Pedido",
                "Preencha todos os campos para inserir na tabela.",
                "Salvar pedido"
        );
        return "formulario";
    }

    @PostMapping(value = "/pedidos", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String salvar(
            @ModelAttribute FormularioPedidoDTO formularioPedido,
            RedirectAttributes redirectAttributes
    ) {
        pedidoService.salvarFormulario(formularioPedido);
        redirectAttributes.addFlashAttribute("sucesso", "Pedido cadastrado com sucesso.");
        return "redirect:/pedidos/novo";
    }

    @GetMapping("/pedidos/{id}/editar")
    public String editar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        FormularioPedidoDTO formularioPedido;
        try {
            formularioPedido = pedidoService.criarFormulario(id);
        } catch (PedidoNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido não encontrado.");
            return "redirect:/pedidos";
        }

        formularioPedidoService.prepararPaginaFormulario(
                model,
                formularioPedido,
                "/pedidos/" + id + "/editar",
                "Editar Pedido",
                "Atualize os dados do pedido selecionado.",
                "Salvar alterações"
        );
        return "formulario";
    }

    @PostMapping("/pedidos/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute FormularioPedidoDTO formularioPedido,
            RedirectAttributes redirectAttributes
    ) {
        try {
            pedidoService.atualizarFormulario(id, formularioPedido);
        } catch (PedidoNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido não encontrado.");
            return "redirect:/pedidos";
        }

        redirectAttributes.addFlashAttribute("sucesso", "Pedido atualizado com sucesso.");
        return "redirect:/pedidos/" + id + "/editar";
    }

    @PostMapping("/pedidos/{id}/excluir")
    public String excluirView(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.excluir(id);
        } catch (PedidoNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido não encontrado.");
            return "redirect:/pedidos";
        }
        redirectAttributes.addFlashAttribute("sucesso", "Pedido excluído com sucesso.");
        return "redirect:/pedidos";
    }

    @GetMapping("/pedidos/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        byte[] pdf;
        try {
            pdf = pedidoService.gerarPdf(id);
        } catch (PedidoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }

        String nomeArquivo = "pedido-" + id + ".pdf";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename(nomeArquivo)
                                .build()
                                .toString()
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}