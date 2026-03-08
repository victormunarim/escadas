package com.example.demo.web;

import com.example.demo.pedidos.Pedido;
import com.example.demo.pedidos.FormularioPedido;
import com.example.demo.pedidos.RepositorioPedido;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControladorPedidos {

    private final RepositorioPedido repositorioPedido;

    public ControladorPedidos(RepositorioPedido repositorioPedido) {
        this.repositorioPedido = repositorioPedido;
    }

    @GetMapping("/pedidos")
    public String listar(Model model) {
        model.addAttribute("pedidos", repositorioPedido.findAll());
        return "redirect:/crud/pedidos";
    }

    @GetMapping("/pedidos/novo")
    public String novoPedido(Model model) {
        prepararPaginaFormulario(
                model,
                new FormularioPedido(),
                "/pedidos",
                "Novo Pedido",
                "Preencha todos os campos (exceto flag oculto) para inserir na tabela.",
                "Salvar pedido"
        );
        return "pedido-form";
    }

    @PostMapping("/pedidos")
    public String salvar(
            @ModelAttribute FormularioPedido formularioPedido,
            RedirectAttributes redirectAttributes
    ) {
        Pedido pedido = new Pedido();
        aplicarFormularioNoPedido(formularioPedido, pedido);
        pedido.setFlagOculto(Boolean.FALSE);

        repositorioPedido.save(pedido);
        redirectAttributes.addFlashAttribute("sucesso", "Pedido cadastrado com sucesso.");
        return "redirect:/pedidos/novo";
    }

    @GetMapping("/pedidos/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Pedido pedido = repositorioPedido.findById(id).orElse(null);

        if (pedido == null) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido não encontrado.");
            return "redirect:/crud/pedidos";
        }

        FormularioPedido formularioPedido = criarFormularioDePedido(pedido);
        prepararPaginaFormulario(
                model,
                formularioPedido,
                "/pedidos/" + id + "/editar",
                "Editar Pedido",
                "Atualize os dados do pedido selecionado.",
                "Salvar alterações"
        );
        return "pedido-form";
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

        aplicarFormularioNoPedido(formularioPedido, pedido);
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

    private void prepararPaginaFormulario(
            Model model,
            FormularioPedido formularioPedido,
            String urlFormulario,
            String tituloPagina,
            String subtituloPagina,
            String textoBotaoSalvar
    ) {
        model.addAttribute("formularioPedido", formularioPedido);
        model.addAttribute("urlFormulario", urlFormulario);
        model.addAttribute("tituloPagina", tituloPagina);
        model.addAttribute("subtituloPagina", subtituloPagina);
        model.addAttribute("textoBotaoSalvar", textoBotaoSalvar);
    }

    private FormularioPedido criarFormularioDePedido(Pedido pedido) {
        FormularioPedido formulario = new FormularioPedido();
        formulario.setNumeroPedido(pedido.getNumeroPedido());
        formulario.setNomeCliente(pedido.getNomeCliente());
        formulario.setEmail(pedido.getEmail());
        formulario.setCpf(pedido.getCpf());
        formulario.setRg(pedido.getRg());
        formulario.setCnpj(pedido.getCnpj());
        formulario.setServicoSocial(pedido.getServicoSocial());
        formulario.setProfissao(pedido.getProfissao());
        formulario.setAdmObra(pedido.getAdmObra());
        formulario.setTelefone(pedido.getTelefone());
        formulario.setTelefoneFixo(pedido.getTelefoneFixo());
        formulario.setDescricao(pedido.getDescricao());
        formulario.setAcabamento(pedido.getAcabamento());
        formulario.setTubos(pedido.getTubos());
        formulario.setRevestimento(pedido.getRevestimento());
        formulario.setValorTotal(pedido.getValorTotal());
        formulario.setPrazoMontagem(pedido.getPrazoMontagem());
        formulario.setNumero(pedido.getNumero());
        formulario.setBairro(pedido.getBairro());
        formulario.setCidade(pedido.getCidade());
        formulario.setCep(pedido.getCep());
        formulario.setReferencia(pedido.getReferencia());
        formulario.setNumeroCliente(pedido.getNumeroCliente());
        formulario.setBairroCliente(pedido.getBairroCliente());
        formulario.setCidadeCliente(pedido.getCidadeCliente());
        formulario.setCepCliente(pedido.getCepCliente());
        formulario.setReferenciaCliente(pedido.getReferenciaCliente());
        formulario.setCliente(pedido.getCliente());
        formulario.setValor(pedido.getValor());
        return formulario;
    }

    private void aplicarFormularioNoPedido(FormularioPedido formularioPedido, Pedido pedido) {
        pedido.setNumeroPedido(formularioPedido.getNumeroPedido());
        pedido.setNomeCliente(formularioPedido.getNomeCliente());
        pedido.setEmail(formularioPedido.getEmail());
        pedido.setCpf(formularioPedido.getCpf());
        pedido.setRg(formularioPedido.getRg());
        pedido.setCnpj(formularioPedido.getCnpj());
        pedido.setServicoSocial(formularioPedido.getServicoSocial());
        pedido.setProfissao(formularioPedido.getProfissao());
        pedido.setAdmObra(formularioPedido.getAdmObra());
        pedido.setTelefone(formularioPedido.getTelefone());
        pedido.setTelefoneFixo(formularioPedido.getTelefoneFixo());
        pedido.setDescricao(formularioPedido.getDescricao());
        pedido.setAcabamento(formularioPedido.getAcabamento());
        pedido.setTubos(formularioPedido.getTubos());
        pedido.setRevestimento(formularioPedido.getRevestimento());
        pedido.setValorTotal(formularioPedido.getValorTotal());
        pedido.setPrazoMontagem(formularioPedido.getPrazoMontagem());
        pedido.setNumero(formularioPedido.getNumero());
        pedido.setBairro(formularioPedido.getBairro());
        pedido.setCidade(formularioPedido.getCidade());
        pedido.setCep(formularioPedido.getCep());
        pedido.setReferencia(formularioPedido.getReferencia());
        pedido.setNumeroCliente(formularioPedido.getNumeroCliente());
        pedido.setBairroCliente(formularioPedido.getBairroCliente());
        pedido.setCidadeCliente(formularioPedido.getCidadeCliente());
        pedido.setCepCliente(formularioPedido.getCepCliente());
        pedido.setReferenciaCliente(formularioPedido.getReferenciaCliente());
        pedido.setCliente(formularioPedido.getCliente());
        pedido.setValor(formularioPedido.getValor());
    }
}
