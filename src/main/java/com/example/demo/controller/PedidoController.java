package com.example.demo.controller;

import com.example.demo.dto.ArquivoPedidoDTO;
import com.example.demo.dto.FormularioPedidoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.dto.PedidoResumoDTO;
import com.example.demo.constants.ColunasPedido;
import com.example.demo.crud.ModuloCrud;
import com.example.demo.crud.OpcaoCrud;
import com.example.demo.crud.filtros.FiltrosCrudBuilder;
import com.example.demo.crud.filtros.OpcoesDataCrud;
import com.example.demo.exception.PedidoNaoEncontradoException;
import com.example.demo.service.FormularioPedidoService;
import com.example.demo.service.PedidoService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.crud.ColunaCrud.col;


@Controller
public class PedidoController {
    private final PedidoService pedidoService;
    private final FormularioPedidoService formularioPedidoService;

    public PedidoController(
            PedidoService pedidoService,
            FormularioPedidoService formularioPedidoService
    ) {
        this.pedidoService = pedidoService;
        this.formularioPedidoService = formularioPedidoService;
    }

    @GetMapping("/crud/pedidos")
    public String crudPedidos(
            @RequestParam Map<String, String> parametros,
            Model model
    ) {
        Map<String, String> parametrosEfetivos = new java.util.HashMap<>(parametros);
        parametrosEfetivos.putIfAbsent("size", "50");

        List<PedidoResumoDTO> linhas = pedidoService.listarResumo(parametrosEfetivos);

        model.addAttribute("modulo", moduloCrudPedidos());
        model.addAttribute("parametros", parametrosEfetivos);
        model.addAttribute("linhas", linhas);

        return "index";
    }

    private ModuloCrud moduloCrudPedidos() {
        List<OpcaoCrud> opcoesQuantidade = List.of(
                new OpcaoCrud("50", "50"),
                new OpcaoCrud("100", "100"),
                new OpcaoCrud("200", "200"),
                new OpcaoCrud("500", "500")
        );

        return new ModuloCrud(
                "pedidos",
                "Pedidos",
                "/crud/pedidos",
                FiltrosCrudBuilder.criar()
                        .texto("busca", "Busca", "")
                        .texto("numero_busca", "Numero do pedido", "")
                        .selecao("dia", "Dia", OpcoesDataCrud.DIAS)
                        .selecao("mes", "Mês", OpcoesDataCrud.MESES)
                        .selecao("ano", "Ano", OpcoesDataCrud.ANOS)
                        .selecao("size", "Quantidade", opcoesQuantidade)
                        .build(),
                List.of(
                        col(ColunasPedido.LABEL_ID_PEDIDO, ColunasPedido.ID_PEDIDO),
                        col(ColunasPedido.LABEL_NUMERO_PEDIDO, ColunasPedido.NUMERO_PEDIDO),
                        col(ColunasPedido.LABEL_CLIENTE_NOME, ColunasPedido.CLIENTE_NOME),
                        col(ColunasPedido.LABEL_EMAIL, ColunasPedido.EMAIL),
                        col(ColunasPedido.LABEL_CPF, ColunasPedido.CPF),
                        col(ColunasPedido.LABEL_RG, ColunasPedido.RG),
                        col(ColunasPedido.LABEL_CNPJ, ColunasPedido.CNPJ),
                        col(ColunasPedido.LABEL_SERVICO_SOCIAL, ColunasPedido.SERVICO_SOCIAL),
                        col(ColunasPedido.LABEL_PROFISSAO, ColunasPedido.PROFISSAO),
                        col(ColunasPedido.LABEL_ADM_OBRA, ColunasPedido.ADM_OBRA),
                        col(ColunasPedido.LABEL_TELEFONE, ColunasPedido.TELEFONE),
                        col(ColunasPedido.LABEL_TELEFONE_FIXO, ColunasPedido.TELEFONE_FIXO),
                        col(ColunasPedido.LABEL_DESCRICAO, ColunasPedido.DESCRICAO),
                        col(ColunasPedido.LABEL_ACABAMENTO, ColunasPedido.ACABAMENTO),
                        col(ColunasPedido.LABEL_TUBOS, ColunasPedido.TUBOS),
                        col(ColunasPedido.LABEL_REVESTIMENTO, ColunasPedido.REVESTIMENTO),
                        col(ColunasPedido.LABEL_VALOR_TOTAL, ColunasPedido.VALOR_TOTAL),
                        col(ColunasPedido.LABEL_PRAZO_MONTAGEM, ColunasPedido.PRAZO_MONTAGEM),
                        col(ColunasPedido.LABEL_DATA_CADASTRO, ColunasPedido.DATA_CADASTRO),
                        col(ColunasPedido.LABEL_VALOR, ColunasPedido.VALOR)
                )
        );
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
        enderecoObra.put("BairroEntity", FormatacaoUtil.formatarTexto(pedido.getBairro()));
        enderecoObra.put("Município", FormatacaoUtil.formatarTexto(pedido.getMunicipio()));
        enderecoObra.put("CEP", FormatacaoUtil.formatarCep(pedido.getCep()));
        enderecoObra.put("Referência", FormatacaoUtil.formatarTexto(pedido.getReferencia()));

        Map<String, String> enderecoCliente = new LinkedHashMap<>();
        enderecoCliente.put("Número", FormatacaoUtil.formatarTexto(pedido.getNumeroCliente()));
        enderecoCliente.put("BairroEntity", FormatacaoUtil.formatarTexto(pedido.getBairroCliente()));
        enderecoCliente.put("Município", FormatacaoUtil.formatarTexto(pedido.getMunicipioCliente()));
        enderecoCliente.put("CEP", FormatacaoUtil.formatarCep(pedido.getCepCliente()));
        enderecoCliente.put("Referência", FormatacaoUtil.formatarTexto(pedido.getReferenciaCliente()));

        List<ArquivoPedidoDTO> arquivosPedido = pedidoService.listarArquivos(id);

        model.addAttribute("pedidoId", pedido.getId());
        model.addAttribute("resumoPedido", resumoPedido);
        model.addAttribute("dadosCliente", dadosCliente);
        model.addAttribute("enderecoObra", enderecoObra);
        model.addAttribute("enderecoCliente", enderecoCliente);
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
            return "redirect:/crud/pedidos";
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
            return "redirect:/crud/pedidos";
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
            return "redirect:/crud/pedidos";
        }
        redirectAttributes.addFlashAttribute("sucesso", "Pedido excluído com sucesso.");
        return "redirect:/crud/pedidos";
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
