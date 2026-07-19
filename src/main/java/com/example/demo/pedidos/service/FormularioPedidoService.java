package com.example.demo.pedidos.service;
import com.example.demo.pedidos.model.PedidoEntity;
import com.example.demo.pedidos.dto.*;
import com.example.demo.shared.crud.formulario.*;
import com.example.demo.shared.crud.render.*;
import com.example.demo.pedidos.config.ColunasPedido;
import com.example.demo.pedidos.config.FormularioPedidoViewConfig;

import com.example.demo.shared.crud.OpcaoCrud;
import com.example.demo.localidades.model.BairroEntity;
import com.example.demo.localidades.model.EstadoEntity;
import com.example.demo.localidades.model.MunicipioEntity;
import com.example.demo.localidades.service.ConsultaLocalidadesService;
import com.example.demo.shared.util.FormatacaoUtil;
import com.example.demo.shared.util.NumeroUtil;
import com.example.demo.localidades.repository.BairroRepository;
import com.example.demo.localidades.repository.EstadoRepository;
import com.example.demo.localidades.repository.MunicipioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormularioPedidoService {

    private final ConsultaLocalidadesService consultaLocalidades;
    private final EstadoRepository estadoRepository;
    private final MunicipioRepository municipioRepository;
    private final BairroRepository bairroRepository;

    public FormularioPedidoService(
            ConsultaLocalidadesService consultaLocalidades,
            EstadoRepository estadoRepository,
            MunicipioRepository municipioRepository,
            BairroRepository bairroRepository
    ) {
        this.consultaLocalidades = consultaLocalidades;
        this.estadoRepository = estadoRepository;
        this.municipioRepository = municipioRepository;
        this.bairroRepository = bairroRepository;
    }

    public FormularioPedidoDTO criarFormularioDePedido(PedidoEntity pedido) {
        FormularioPedidoDTO formulario = new FormularioPedidoDTO();
        formulario.setNumeroPedido(pedido.getNumeroPedido());
        formulario.setNomeCliente(pedido.getNomeCliente());
        formulario.setEmail(pedido.getEmail());
        formulario.setCpf(NumeroUtil.longParaTexto(pedido.getCpf()));
        formulario.setRg(NumeroUtil.inteiroParaTexto(pedido.getRg()));
        formulario.setCnpj(pedido.getCnpj());
        formulario.setInscricaoEstadual(pedido.getInscricaoEstadual());
        formulario.setProfissao(pedido.getProfissao());
        formulario.setAdmObra(pedido.getAdmObra());
        formulario.setTelefone(NumeroUtil.somenteDigitos(pedido.getTelefone()));
        formulario.setTelefoneFixo(NumeroUtil.somenteDigitos(pedido.getTelefoneFixo()));
        formulario.setDescricao(pedido.getDescricao());
        formulario.setAcabamento(pedido.getAcabamento());
        formulario.setTubos(pedido.getTubos());
        formulario.setRevestimento(pedido.getRevestimento());
        formulario.setValorTotal(pedido.getValorTotal());
        formulario.setPrazoMontagem(pedido.getPrazoMontagem());
        formulario.setNumero(NumeroUtil.inteiroParaTexto(pedido.getNumero()));
        formulario.setUf(resolverUf(pedido.getEstadoId()));
        formulario.setBairro(resolverBairroNome(pedido.getBairroId()));
        formulario.setMunicipio(resolverMunicipioNome(pedido.getMunicipioId()));
        formulario.setCep(FormatacaoUtil.formatarCep(pedido.getCep()));
        formulario.setReferencia(pedido.getReferencia());
        formulario.setNumeroCliente(NumeroUtil.inteiroParaTexto(pedido.getNumeroCliente()));
        formulario.setUfCliente(resolverUf(pedido.getEstadoClienteId()));
        formulario.setBairroCliente(resolverBairroNome(pedido.getBairroClienteId()));
        formulario.setMunicipioCliente(resolverMunicipioNome(pedido.getMunicipioClienteId()));
        formulario.setCepCliente(FormatacaoUtil.formatarCep(pedido.getCepCliente()));
        formulario.setReferenciaCliente(pedido.getReferenciaCliente());
        formulario.setValor(pedido.getValor());
        return formulario;
    }

    public void aplicarFormularioNoPedido(FormularioPedidoDTO formularioPedido, PedidoEntity pedido) {
        pedido.setNumeroPedido(zeroSeNulo(formularioPedido.getNumeroPedido()));
        pedido.setNomeCliente(vazioSeNulo(formularioPedido.getNomeCliente()));
        pedido.setEmail(vazioSeNulo(formularioPedido.getEmail()));
        pedido.setCpf(NumeroUtil.paraLong(formularioPedido.getCpf(), 11));
        pedido.setRg(NumeroUtil.paraInteiro(formularioPedido.getRg(), 7));
        pedido.setCnpj(limitarString(formularioPedido.getCnpj(), 14));
        pedido.setInscricaoEstadual(vazioSeNulo(formularioPedido.getInscricaoEstadual()));
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
        pedido.setEstadoId(resolverEstadoId(formularioPedido.getUf()));
        pedido.setMunicipioId(resolverMunicipioId(formularioPedido.getUf(), formularioPedido.getMunicipio()));
        pedido.setBairroId(resolverBairroId(
                formularioPedido.getMunicipio(), formularioPedido.getBairro(), pedido.getMunicipioId()
        ));
        pedido.setCep(NumeroUtil.paraInteiro(formularioPedido.getCep(), 8));
        pedido.setReferencia(vazioSeNulo(formularioPedido.getReferencia()));
        pedido.setNumeroCliente(NumeroUtil.paraInteiro(formularioPedido.getNumeroCliente(), 10));
        pedido.setEstadoClienteId(resolverEstadoId(formularioPedido.getUfCliente()));
        pedido.setMunicipioClienteId(resolverMunicipioId(
                formularioPedido.getUfCliente(), formularioPedido.getMunicipioCliente()
        ));
        pedido.setBairroClienteId(resolverBairroId(
                formularioPedido.getMunicipioCliente(), formularioPedido.getBairroCliente(),
                pedido.getMunicipioClienteId()
        ));
        pedido.setCepCliente(NumeroUtil.paraInteiro(formularioPedido.getCepCliente(), 8));
        pedido.setReferenciaCliente(vazioSeNulo(formularioPedido.getReferenciaCliente()));
        pedido.setValor(formularioPedido.getValor());
    }


    private String vazioSeNulo(String valor) {
        return valor == null ? "" : valor;
    }

    private String limitarString(String valor, int max) {
        if (valor == null) {
            return "";
        }
        String texto = valor.trim();
        return texto.length() > max ? texto.substring(0, max) : texto;
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

    private String resolverUf(Integer estadoId) {
        if (estadoId == null) {
            return "SC";
        }
        return estadoRepository.findById(estadoId)
                .map(EstadoEntity::getSigla)
                .filter(valor -> !valor.isBlank())
                .orElse("SC");
    }

    private String resolverMunicipioNome(Integer municipioId) {
        if (municipioId == null) {
            return "";
        }
        return municipioRepository.findById(municipioId)
                .map(MunicipioEntity::getNome)
                .orElse("");
    }

    private String resolverBairroNome(Integer bairroId) {
        if (bairroId == null) {
            return "";
        }
        return bairroRepository.findById(bairroId)
                .map(BairroEntity::getNome)
                .orElse("");
    }

    private Integer resolverEstadoId(String uf) {
        if (uf == null || uf.isBlank()) {
            return null;
        }
        return estadoRepository.findBySiglaIgnoreCase(uf.trim())
                .map(EstadoEntity::getId)
                .orElse(null);
    }

    private Integer resolverMunicipioId(String uf, String municipioNome) {
        Integer estadoId = resolverEstadoId(uf);
        if (estadoId == null || municipioNome == null || municipioNome.isBlank()) {
            return null;
        }
        return municipioRepository.findByNomeIgnoreCaseAndEstadoId(municipioNome.trim(), estadoId)
                .map(MunicipioEntity::getId)
                .orElse(null);
    }

    private Integer resolverBairroId(String municipioNome, String bairroNome, Integer municipioId) {
        if (municipioId == null || bairroNome == null || bairroNome.isBlank()) {
            return null;
        }
        return bairroRepository.findByNomeIgnoreCaseAndMunicipioId(bairroNome.trim(), municipioId)
                .map(BairroEntity::getId)
                .orElse(null);
    }

    private List<OpcaoCrud> opcoesDoValoresAtuais(String... valoresAtuais) {
        return Arrays.stream(valoresAtuais)
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
            opcoes.addFirst(new OpcaoCrud(ufNormalizada, ufNormalizada));
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
                opcoes.addFirst(new OpcaoCrud(valorAtual, valorAtual));
            }
        }

        return opcoes;
    }

    private List<OpcaoCrud> opcoesBairrosPorUf(String uf, String... valoresAtuais) {
        if (uf == null || uf.isBlank()) {
            return opcoesDoValoresAtuais(valoresAtuais);
        }

        List<OpcaoCrud> opcoes = consultaLocalidades.buscarBairros("", uf, 500).stream()
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
                opcoes.addFirst(new OpcaoCrud(valorAtual, valorAtual));
            }
        }

        return opcoes;
    }

    public List<CampoRender> obterCamposRender(FormularioPedidoDTO formularioPedido) {
        List<CampoFormularioCrud<FormularioPedidoDTO>> camposBase = FormularioPedidoViewConfig.criarCampos(
                opcoesUf(formularioPedido.getUf()),
                opcoesUf(formularioPedido.getUfCliente()),
                opcoesBairrosPorUf(
                        formularioPedido.getUf(),
                        formularioPedido.getBairro()
                ),
                opcoesBairrosPorUf(
                        formularioPedido.getUfCliente(),
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
        );

        List<CampoRender> camposRender = new ArrayList<>();

        for (CampoFormularioCrud<FormularioPedidoDTO> base : camposBase) {
            // Seções do formulário
            if (ColunasPedido.CAMPO_UF.equals(base.nome())) {
                camposRender.add(new SecaoRender("Endereço da Obra"));
            } else if (ColunasPedido.CAMPO_UF_CLIENTE.equals(base.nome())) {
                camposRender.add(new SecaoRender("Endereço do Cliente"));
            } else if (ColunasPedido.CAMPO_VALOR.equals(base.nome())) {
                camposRender.add(new SecaoRender("Financeiro & Descrição"));
            }

            camposRender.add(base.renderizar(formularioPedido));
        }

        return camposRender;
    }
}