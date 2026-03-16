package com.example.demo.service;

import com.example.demo.constants.ColunasPedido;
import com.example.demo.dto.FormularioPedidoDTO;
import com.example.demo.entity.PedidoEntity;
import com.example.demo.repository.ConsultaLocalidadesRepository;
import com.example.demo.crud.OpcaoCrud;
import com.example.demo.crud.formulario.CampoFormularioCrud;
import com.example.demo.crud.formulario.CamposFormularioCrud;
import com.example.demo.util.FormatacaoUtil;
import com.example.demo.util.NumeroUtil;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormularioPedidoService {

    private final ConsultaLocalidadesRepository consultaLocalidades;

    public FormularioPedidoService(ConsultaLocalidadesRepository consultaLocalidades) {
        this.consultaLocalidades = consultaLocalidades;
    }

    public List<String> buscarBairros(String termo, String uf, int limite) {
        return consultaLocalidades.buscarBairros(termo, uf, limite);
    }

    public List<String> buscarMunicipios(String termo, String uf, int limite) {
        return consultaLocalidades.buscarMunicipios(termo, uf, limite);
    }

    public void prepararPaginaFormulario(
            Model model,
            FormularioPedidoDTO formularioPedido,
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
        model.addAttribute("urlVoltar", "/crud/pedidos");
        model.addAttribute(
                "camposFormularioPedido",
                camposFormularioPedido(
                        opcoesUf(formularioPedido.getUf()),
                        opcoesUf(formularioPedido.getUfCliente()),
                        opcoesDoValoresAtuais(
                                formularioPedido.getBairro(),
                                formularioPedido.getBairroCliente()
                        ),
                        opcoesMunicipiosPorUf(
                                formularioPedido.getUf(),
                                formularioPedido.getMunicipio()
                        ),
                        opcoesMunicipiosPorUf(
                                formularioPedido.getUfCliente(),
                                formularioPedido.getMunicipioCliente()
                        )
                )
        );
    }

    public FormularioPedidoDTO criarFormularioDePedido(PedidoEntity pedido) {
        FormularioPedidoDTO formulario = new FormularioPedidoDTO();
        formulario.setNumeroPedido(pedido.getNumeroPedido());
        formulario.setNomeCliente(pedido.getNomeCliente());
        formulario.setEmail(pedido.getEmail());
        formulario.setCpf(FormatacaoUtil.formatarCpf(pedido.getCpf()));
        formulario.setRg(FormatacaoUtil.formatarRg(pedido.getRg()));
        formulario.setCnpj(pedido.getCnpj());
        formulario.setServicoSocial(pedido.getServicoSocial());
        formulario.setProfissao(pedido.getProfissao());
        formulario.setAdmObra(pedido.getAdmObra());
        formulario.setTelefone(FormatacaoUtil.formatarTelefoneCelular(pedido.getTelefone()));
        formulario.setTelefoneFixo(FormatacaoUtil.formatarTelefoneFixo(pedido.getTelefoneFixo()));
        formulario.setDescricao(pedido.getDescricao());
        formulario.setAcabamento(pedido.getAcabamento());
        formulario.setTubos(pedido.getTubos());
        formulario.setRevestimento(pedido.getRevestimento());
        formulario.setValorTotal(pedido.getValorTotal());
        formulario.setPrazoMontagem(pedido.getPrazoMontagem());
        formulario.setNumero(NumeroUtil.inteiroParaTexto(pedido.getNumero()));
        formulario.setBairro(pedido.getBairro());
        formulario.setMunicipio(pedido.getMunicipio());
        formulario.setCep(FormatacaoUtil.formatarCep(pedido.getCep()));
        formulario.setReferencia(pedido.getReferencia());
        formulario.setNumeroCliente(NumeroUtil.inteiroParaTexto(pedido.getNumeroCliente()));
        formulario.setBairroCliente(pedido.getBairroCliente());
        formulario.setMunicipioCliente(pedido.getMunicipioCliente());
        formulario.setCepCliente(FormatacaoUtil.formatarCep(pedido.getCepCliente()));
        formulario.setReferenciaCliente(pedido.getReferenciaCliente());
        formulario.setValor(pedido.getValor());
        return formulario;
    }

    public void aplicarFormularioNoPedido(FormularioPedidoDTO formularioPedido, PedidoEntity pedido) {
        pedido.setNumeroPedido(zeroSeNulo(formularioPedido.getNumeroPedido()));
        pedido.setNomeCliente(vazioSeNulo(formularioPedido.getNomeCliente()));
        pedido.setEmail(vazioSeNulo(formularioPedido.getEmail()));
        pedido.setCpf(NumeroUtil.paraLong(formularioPedido.getCpf(), 14));
        pedido.setRg(NumeroUtil.paraInteiro(formularioPedido.getRg(), 12));
        pedido.setCnpj(vazioSeNulo(formularioPedido.getCnpj()));
        pedido.setServicoSocial(vazioSeNulo(formularioPedido.getServicoSocial()));
        pedido.setProfissao(vazioSeNulo(formularioPedido.getProfissao()));
        pedido.setAdmObra(vazioSeNulo(formularioPedido.getAdmObra()));
        pedido.setTelefone(NumeroUtil.somenteDigitosLimitado(formularioPedido.getTelefone(), 20));
        pedido.setTelefoneFixo(NumeroUtil.somenteDigitosLimitado(formularioPedido.getTelefoneFixo(), 20));
        pedido.setDescricao(vazioSeNulo(formularioPedido.getDescricao()));
        pedido.setAcabamento(vazioSeNulo(formularioPedido.getAcabamento()));
        pedido.setTubos(vazioSeNulo(formularioPedido.getTubos()));
        pedido.setRevestimento(falsoSeNulo(formularioPedido.getRevestimento()));
        pedido.setValorTotal(zeroSeNulo(formularioPedido.getValorTotal()));
        pedido.setPrazoMontagem(zeroSeNulo(formularioPedido.getPrazoMontagem()));
        pedido.setNumero(NumeroUtil.paraInteiro(formularioPedido.getNumero(), 10));
        pedido.setBairro(vazioSeNulo(formularioPedido.getBairro()));
        pedido.setMunicipio(vazioSeNulo(formularioPedido.getMunicipio()));
        pedido.setCep(NumeroUtil.paraInteiro(formularioPedido.getCep(), 8));
        pedido.setReferencia(vazioSeNulo(formularioPedido.getReferencia()));
        pedido.setNumeroCliente(NumeroUtil.paraInteiro(formularioPedido.getNumeroCliente(), 10));
        pedido.setBairroCliente(vazioSeNulo(formularioPedido.getBairroCliente()));
        pedido.setMunicipioCliente(vazioSeNulo(formularioPedido.getMunicipioCliente()));
        pedido.setCepCliente(NumeroUtil.paraInteiro(formularioPedido.getCepCliente(), 8));
        pedido.setReferenciaCliente(vazioSeNulo(formularioPedido.getReferenciaCliente()));
        pedido.setValor(zeroSeNulo(formularioPedido.getValor()));
    }

    private List<CampoFormularioCrud> camposFormularioPedido(
            List<OpcaoCrud> opcoesUf,
            List<OpcaoCrud> opcoesUfCliente,
            List<OpcaoCrud> opcoesBairro,
            List<OpcaoCrud> opcoesMunicipio,
            List<OpcaoCrud> opcoesMunicipioCliente
    ) {
        return List.of(
                CamposFormularioCrud.numero(
                        ColunasPedido.CAMPO_NUMERO_PEDIDO,
                        ColunasPedido.LABEL_NUMERO_PEDIDO,
                        null,
                        true,
                        "1",
                        null,
                        "campo--numero-pedido"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_CLIENTE_NOME,
                        ColunasPedido.LABEL_CLIENTE_NOME,
                        null,
                        true,
                        120,
                        "campo--cliente-nome"
                ),
                CamposFormularioCrud.email(
                        ColunasPedido.CAMPO_EMAIL,
                        ColunasPedido.LABEL_EMAIL,
                        null,
                        false,
                        120,
                        "campo--email"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_CPF,
                        ColunasPedido.LABEL_CPF,
                        null,
                        false,
                        14,
                        "campo--cpf"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_RG,
                        ColunasPedido.LABEL_RG,
                        null,
                        false,
                        12,
                        "campo--rg"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_CNPJ,
                        ColunasPedido.LABEL_CNPJ,
                        null,
                        false,
                        30,
                        "campo--cnpj"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_SERVICO_SOCIAL,
                        ColunasPedido.LABEL_SERVICO_SOCIAL,
                        null,
                        false,
                        100,
                        "campo--servico-social"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_PROFISSAO,
                        ColunasPedido.LABEL_PROFISSAO,
                        null,
                        false,
                        100,
                        "campo--profissao"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_ADM_OBRA,
                        ColunasPedido.LABEL_ADM_OBRA,
                        null,
                        false,
                        100,
                        "campo--adm-obra"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_TELEFONE,
                        ColunasPedido.LABEL_TELEFONE,
                        null,
                        false,
                        20,
                        "campo--telefone"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_TELEFONE_FIXO,
                        ColunasPedido.LABEL_TELEFONE_FIXO,
                        null,
                        false,
                        20,
                        "campo--telefone-fixo"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_ACABAMENTO,
                        ColunasPedido.LABEL_ACABAMENTO,
                        null,
                        false,
                        120,
                        "campo--acabamento"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_TUBOS,
                        ColunasPedido.LABEL_TUBOS,
                        null,
                        false,
                        120,
                        "campo--tubos"
                ),
                CamposFormularioCrud.selecao(
                        ColunasPedido.CAMPO_REVESTIMENTO,
                        ColunasPedido.LABEL_REVESTIMENTO,
                        false,
                        List.of(new OpcaoCrud("true", "Sim"), new OpcaoCrud("false", "Não")),
                        "campo--revestimento"
                ),
                CamposFormularioCrud.numero(
                        ColunasPedido.CAMPO_VALOR_TOTAL,
                        ColunasPedido.LABEL_VALOR_TOTAL,
                        null,
                        false,
                        "0",
                        "0.01",
                        "campo--valor-total"
                ),
                CamposFormularioCrud.numero(
                        ColunasPedido.CAMPO_PRAZO_MONTAGEM,
                        ColunasPedido.LABEL_PRAZO_MONTAGEM,
                        null,
                        false,
                        "0",
                        null,
                        "campo--prazo-montagem"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_NUMERO,
                        ColunasPedido.LABEL_NUMERO,
                        null,
                        false,
                        10,
                        "campo--numero"
                ),
                CamposFormularioCrud.selecao(
                        ColunasPedido.CAMPO_UF,
                        ColunasPedido.LABEL_UF,
                        true,
                        opcoesUf,
                        "campo--uf"
                ),
                CamposFormularioCrud.selecao(
                        ColunasPedido.CAMPO_MUNICIPIO,
                        ColunasPedido.LABEL_MUNICIPIO,
                        false,
                        opcoesMunicipio,
                        "campo--municipio"
                ),
                CamposFormularioCrud.selecao(
                        ColunasPedido.CAMPO_BAIRRO,
                        ColunasPedido.LABEL_BAIRRO,
                        false,
                        opcoesBairro,
                        "campo--bairro"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_CEP,
                        ColunasPedido.LABEL_CEP,
                        null,
                        false,
                        9,
                        "campo--cep"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_REFERENCIA,
                        ColunasPedido.LABEL_REFERENCIA,
                        null,
                        false,
                        200,
                        "campo--referencia"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_NUMERO_CLIENTE,
                        ColunasPedido.LABEL_NUMERO_CLIENTE,
                        null,
                        false,
                        10,
                        "campo--numero-cliente"
                ),
                CamposFormularioCrud.selecao(
                        ColunasPedido.CAMPO_UF_CLIENTE,
                        ColunasPedido.LABEL_UF_CLIENTE,
                        true,
                        opcoesUfCliente,
                        "campo--uf-cliente"
                ),
                CamposFormularioCrud.selecao(
                        ColunasPedido.CAMPO_MUNICIPIO_CLIENTE,
                        ColunasPedido.LABEL_MUNICIPIO_CLIENTE,
                        false,
                        opcoesMunicipioCliente,
                        "campo--municipio-cliente"
                ),
                CamposFormularioCrud.selecao(
                        ColunasPedido.CAMPO_BAIRRO_CLIENTE,
                        ColunasPedido.LABEL_BAIRRO_CLIENTE,
                        false,
                        opcoesBairro,
                        "campo--bairro-cliente"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_CEP_CLIENTE,
                        ColunasPedido.LABEL_CEP_CLIENTE,
                        null,
                        false,
                        9,
                        "campo--cep-cliente"
                ),
                CamposFormularioCrud.texto(
                        ColunasPedido.CAMPO_REFERENCIA_CLIENTE,
                        ColunasPedido.LABEL_REFERENCIA_CLIENTE,
                        null,
                        false,
                        200,
                        "campo--referencia-cliente"
                ),
                CamposFormularioCrud.numero(
                        ColunasPedido.CAMPO_VALOR,
                        ColunasPedido.LABEL_VALOR,
                        null,
                        false,
                        "0",
                        "0.01",
                        "campo--valor"
                ),
                CamposFormularioCrud.textarea(
                        ColunasPedido.CAMPO_DESCRICAO,
                        ColunasPedido.LABEL_DESCRICAO,
                        null,
                        false,
                        4,
                        2000,
                        "filtro-crud--completo campo--descricao"
                )
        );
    }

    private String vazioSeNulo(String valor) {
        return valor == null ? "" : valor;
    }

    private Integer zeroSeNulo(Integer valor) {
        return valor == null ? 0 : valor;
    }

    private BigDecimal zeroSeNulo(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }

    private Boolean falsoSeNulo(Boolean valor) {
        return valor != null && valor;
    }

    private List<OpcaoCrud> opcoesDoValoresAtuais(String... valoresAtuais) {
        return java.util.Arrays.stream(valoresAtuais)
                .filter(valor -> valor != null && !valor.isBlank())
                .distinct()
                .map(valor -> new OpcaoCrud(valor, valor))
                .collect(Collectors.toList());
    }

    private List<OpcaoCrud> opcoesUf(String ufAtual) {
        List<OpcaoCrud> opcoes = consultaLocalidades.buscarUfs().stream()
                .map(uf -> new OpcaoCrud(uf, uf))
                .collect(Collectors.toList());
        String ufNormalizada = (ufAtual == null || ufAtual.isBlank()) ? "" : ufAtual.trim().toUpperCase();
        if (ufNormalizada.isEmpty()) {
            return opcoes;
        }
        boolean existeUfAtual = opcoes.stream().anyMatch(opcao -> ufNormalizada.equals(opcao.valor()));
        if (!existeUfAtual) {
            opcoes = new ArrayList<>(opcoes);
            opcoes.add(0, new OpcaoCrud(ufNormalizada, ufNormalizada));
        }
        return opcoes;
    }

    private List<OpcaoCrud> opcoesMunicipiosPorUf(String uf, String... valoresAtuais) {
        if (uf == null || uf.isBlank()) {
            return opcoesDoValoresAtuais(valoresAtuais);
        }

        List<OpcaoCrud> opcoes = consultaLocalidades.buscarMunicipios("", uf, 500).stream()
                .map(valor -> new OpcaoCrud(valor, valor))
                .collect(Collectors.toCollection(ArrayList::new));

        LinkedHashSet<String> valores = opcoes.stream()
                .map(OpcaoCrud::valor)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (String valorAtual : valoresAtuais) {
            if (valorAtual == null || valorAtual.isBlank()) {
                continue;
            }
            if (valores.add(valorAtual)) {
                opcoes.add(0, new OpcaoCrud(valorAtual, valorAtual));
            }
        }

        return opcoes;
    }
}
