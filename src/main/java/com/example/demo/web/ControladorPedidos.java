package com.example.demo.web;

import com.example.demo.pedidos.localidade.service.ConsultaLocalidades;
import com.example.demo.pedidos.model.FormularioPedido;
import com.example.demo.pedidos.model.Pedido;
import com.example.demo.pedidos.repository.RepositorioPedido;
import com.example.demo.pedidos.service.FormularioPedidoService;
import com.example.demo.pedidos.service.GeradorPdfPedido;
import com.example.demo.shared.crud.OpcaoCrud;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorPedidos {

    private final RepositorioPedido repositorioPedido;
    private final GeradorPdfPedido geradorPdfPedido;
    private final ConsultaLocalidades consultaLocalidades;
    private final FormularioPedidoService formularioPedidoService;

    public ControladorPedidos(
            RepositorioPedido repositorioPedido,
            GeradorPdfPedido geradorPdfPedido,
            ConsultaLocalidades consultaLocalidades,
            FormularioPedidoService formularioPedidoService
    ) {
        this.repositorioPedido = repositorioPedido;
        this.geradorPdfPedido = geradorPdfPedido;
        this.consultaLocalidades = consultaLocalidades;
        this.formularioPedidoService = formularioPedidoService;
    }

    @GetMapping("/pedidos")
    public String listar(Model model) {
        model.addAttribute("pedidos", repositorioPedido.findAll());
        return "redirect:/crud/pedidos";
    }

    @GetMapping("/pedidos/novo")
    public String novoPedido(Model model) {
        formularioPedidoService.prepararPaginaFormulario(
                model,
                new FormularioPedido(),
                "/pedidos",
                "Novo Pedido",
                "Preencha todos os campos para inserir na tabela.",
                "Salvar pedido"
        );
        return "formulario";
    }

    @PostMapping("/pedidos")
    public String salvar(
            @ModelAttribute FormularioPedido formularioPedido,
            RedirectAttributes redirectAttributes
    ) {
        Pedido pedido = new Pedido();
        formularioPedidoService.aplicarFormularioNoPedido(formularioPedido, pedido);
        pedido.setFlagOculto(Boolean.FALSE);

        repositorioPedido.save(pedido);
        redirectAttributes.addFlashAttribute("sucesso", "Pedido cadastrado com sucesso.");
        return "redirect:/pedidos/novo";
    }

    @GetMapping("/pedidos/{id}/editar")
    public String editar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Pedido pedido = repositorioPedido.findById(id).orElse(null);

        if (pedido == null) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido não encontrado.");
            return "redirect:/crud/pedidos";
        }

        FormularioPedido formularioPedido = formularioPedidoService.criarFormularioDePedido(pedido);
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
            @ModelAttribute FormularioPedido formularioPedido,
            RedirectAttributes redirectAttributes
    ) {
        Pedido pedido = repositorioPedido.findById(id).orElse(null);

        if (pedido == null) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido não encontrado.");
            return "redirect:/crud/pedidos";
        }

        formularioPedidoService.aplicarFormularioNoPedido(formularioPedido, pedido);
        repositorioPedido.save(pedido);
        redirectAttributes.addFlashAttribute("sucesso", "Pedido atualizado com sucesso.");
        return "redirect:/pedidos/" + id + "/editar";
    }

    @PostMapping("/pedidos/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Pedido pedido = repositorioPedido.findById(id).orElse(null);

        if (pedido == null) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido não encontrado.");
            return "redirect:/crud/pedidos";
        }

        pedido.setFlagOculto(Boolean.TRUE);
        repositorioPedido.save(pedido);
        redirectAttributes.addFlashAttribute("sucesso", "Pedido excluído com sucesso.");
        return "redirect:/crud/pedidos";
    }

    @GetMapping("/pedidos/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        Pedido pedido = repositorioPedido.findById(id).orElse(null);

        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] pdf = geradorPdfPedido.gerarPdfPedido(pedido);
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

    @GetMapping("/pedidos/localidades/bairros")
    @ResponseBody
    public List<OpcaoCrud> buscarBairros(
            @RequestParam(name = "q", defaultValue = "") String termo,
            @RequestParam(name = "uf", defaultValue = "SC") String uf,
            @RequestParam(name = "limit", defaultValue = "15") Integer limite
    ) {
        return consultaLocalidades.buscarBairros(termo, uf, limite).stream()
                .map(valor -> new OpcaoCrud(valor, valor))
                .collect(Collectors.toList());
    }

    @GetMapping("/pedidos/localidades/municipios")
    @ResponseBody
    public List<OpcaoCrud> buscarMunicipios(
            @RequestParam(name = "q", defaultValue = "") String termo,
            @RequestParam(name = "uf", defaultValue = "SC") String uf,
            @RequestParam(name = "limit", defaultValue = "15") Integer limite
    ) {
        return consultaLocalidades.buscarMunicipios(termo, uf, limite).stream()
                .map(valor -> new OpcaoCrud(valor, valor))
                .collect(Collectors.toList());
    }
}
