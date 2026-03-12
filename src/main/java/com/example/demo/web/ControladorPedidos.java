package com.example.demo.web;

import com.example.demo.pedidos.localidade.service.ConsultaLocalidades;
import com.example.demo.pedidos.arquivo.service.ServicoArquivoPedido;
import com.example.demo.pedidos.model.FormularioPedido;
import com.example.demo.pedidos.model.Pedido;
import com.example.demo.pedidos.repository.RepositorioPedido;
import com.example.demo.pedidos.service.FormularioPedidoService;
import com.example.demo.pedidos.service.GeradorPdfPedido;
import com.example.demo.shared.crud.OpcaoCrud;
import com.example.demo.util.FormatacaoUtil;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ControladorPedidos {

    private final RepositorioPedido repositorioPedido;
    private final GeradorPdfPedido geradorPdfPedido;
    private final ConsultaLocalidades consultaLocalidades;
    private final FormularioPedidoService formularioPedidoService;
    private final ServicoArquivoPedido servicoArquivoPedido;

    public ControladorPedidos(
            RepositorioPedido repositorioPedido,
            GeradorPdfPedido geradorPdfPedido,
            ConsultaLocalidades consultaLocalidades,
            FormularioPedidoService formularioPedidoService,
            ServicoArquivoPedido servicoArquivoPedido
    ) {
        this.repositorioPedido = repositorioPedido;
        this.geradorPdfPedido = geradorPdfPedido;
        this.consultaLocalidades = consultaLocalidades;
        this.formularioPedidoService = formularioPedidoService;
        this.servicoArquivoPedido = servicoArquivoPedido;
    }

    @GetMapping("/pedidos")
    public String listar(Model model) {
        model.addAttribute("pedidos", repositorioPedido.findAll());
        return "redirect:/crud/pedidos";
    }

    @GetMapping("/pedidos/{id}/visualizar")
    public String visualizar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Pedido pedido = repositorioPedido.findById(id).orElse(null);

        if (pedido == null) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido não encontrado.");
            return "redirect:/crud/pedidos";
        }

        Map<String, String> resumoPedido = new LinkedHashMap<>();
        resumoPedido.put("ID do Pedido", FormatacaoUtil.formatarTexto(pedido.getId()));
        resumoPedido.put("Número do Pedido", FormatacaoUtil.formatarTexto(pedido.getNumeroPedido()));
        resumoPedido.put("Data de Cadastro", FormatacaoUtil.formatarDataHora(pedido.getDataCadastro()));
        resumoPedido.put("Valor", FormatacaoUtil.formatarValor(pedido.getValor()));
        resumoPedido.put("Valor Total", FormatacaoUtil.formatarValor(pedido.getValorTotal()));
        resumoPedido.put("Prazo de Montagem", FormatacaoUtil.formatarTexto(pedido.getPrazoMontagem()));
        resumoPedido.put("Acabamento", FormatacaoUtil.formatarTexto(pedido.getAcabamento()));
        resumoPedido.put("Tubos", FormatacaoUtil.formatarTexto(pedido.getTubos()));
        resumoPedido.put("Revestimento", FormatacaoUtil.formatarSimNao(pedido.getRevestimento()));
        resumoPedido.put("Descrição", FormatacaoUtil.formatarTexto(pedido.getDescricao()));

        Map<String, String> dadosCliente = new LinkedHashMap<>();
        dadosCliente.put("Nome", FormatacaoUtil.formatarTexto(pedido.getNomeCliente()));
        dadosCliente.put("E-mail", FormatacaoUtil.formatarTexto(pedido.getEmail()));
        dadosCliente.put("CPF", FormatacaoUtil.formatarCpf(pedido.getCpf()));
        dadosCliente.put("RG", FormatacaoUtil.formatarRg(pedido.getRg()));
        dadosCliente.put("CNPJ", FormatacaoUtil.formatarCnpj(pedido.getCnpj()));
        dadosCliente.put("Serviço Social", FormatacaoUtil.formatarTexto(pedido.getServicoSocial()));
        dadosCliente.put("Profissão", FormatacaoUtil.formatarTexto(pedido.getProfissao()));
        dadosCliente.put("Adm. Obra", FormatacaoUtil.formatarTexto(pedido.getAdmObra()));
        dadosCliente.put("Telefone", FormatacaoUtil.formatarTelefoneCelular(pedido.getTelefone()));
        dadosCliente.put("Telefone Fixo", FormatacaoUtil.formatarTelefoneFixo(pedido.getTelefoneFixo()));

        Map<String, String> enderecoObra = new LinkedHashMap<>();
        enderecoObra.put("Número", FormatacaoUtil.formatarTexto(pedido.getNumero()));
        enderecoObra.put("Bairro", FormatacaoUtil.formatarTexto(pedido.getBairro()));
        enderecoObra.put("Município", FormatacaoUtil.formatarTexto(pedido.getMunicipio()));
        enderecoObra.put("CEP", FormatacaoUtil.formatarCep(pedido.getCep()));
        enderecoObra.put("Referência", FormatacaoUtil.formatarTexto(pedido.getReferencia()));

        Map<String, String> enderecoCliente = new LinkedHashMap<>();
        enderecoCliente.put("Número", FormatacaoUtil.formatarTexto(pedido.getNumeroCliente()));
        enderecoCliente.put("Bairro", FormatacaoUtil.formatarTexto(pedido.getBairroCliente()));
        enderecoCliente.put("Município", FormatacaoUtil.formatarTexto(pedido.getMunicipioCliente()));
        enderecoCliente.put("CEP", FormatacaoUtil.formatarCep(pedido.getCepCliente()));
        enderecoCliente.put("Referência", FormatacaoUtil.formatarTexto(pedido.getReferenciaCliente()));

        model.addAttribute("pedidoId", pedido.getId());
        model.addAttribute("resumoPedido", resumoPedido);
        model.addAttribute("dadosCliente", dadosCliente);
        model.addAttribute("enderecoObra", enderecoObra);
        model.addAttribute("enderecoCliente", enderecoCliente);
        model.addAttribute("arquivosPedido", servicoArquivoPedido.listarPorPedido(id));

        return "pedido-visualizacao";
    }

    @PostMapping("/pedidos/{id}/arquivos")
    public String enviarArquivoPedido(
            @PathVariable Long id,
            @RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirectAttributes
    ) {
        Pedido pedido = repositorioPedido.findById(id).orElse(null);

        if (pedido == null) {
            redirectAttributes.addFlashAttribute("erro", "Pedido não encontrado.");
            return "redirect:/crud/pedidos";
        }

        if (arquivo == null || arquivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Selecione um arquivo para enviar.");
            return "redirect:/pedidos/" + id + "/visualizar";
        }

        try {
            servicoArquivoPedido.enviarERegistrar(pedido, arquivo);
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
