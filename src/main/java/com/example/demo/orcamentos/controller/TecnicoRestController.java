package com.example.demo.orcamentos.controller;

import com.example.demo.auth.security.SecurityUtil;
import com.example.demo.localidades.service.ConsultaLocalidadesService;
import com.example.demo.orcamentos.dto.FormularioOrcamentoDTO;
import com.example.demo.orcamentos.dto.OrcamentoDTO;
import com.example.demo.orcamentos.service.OrcamentoService;
import com.example.demo.pedidos.config.PedidoViewPresenter;
import com.example.demo.pedidos.dto.PedidoDTO;
import com.example.demo.pedidos.service.PedidoService;
import com.example.demo.shared.arquivos.dto.ArquivoDTO;
import com.example.demo.shared.crud.controller.AbstractCrudRestController;
import com.example.demo.shared.crud.render.ListagemDTO;
import com.example.demo.shared.crud.service.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoRestController extends AbstractCrudRestController<FormularioOrcamentoDTO> {

    private final OrcamentoService orcamentoService;
    private final PedidoService pedidoService;
    private final ConsultaLocalidadesService localidadeService;

    public TecnicoRestController(
            OrcamentoService orcamentoService,
            PedidoService pedidoService,
            ConsultaLocalidadesService localidadeService
    ) {
        this.orcamentoService = orcamentoService;
        this.pedidoService = pedidoService;
        this.localidadeService = localidadeService;
    }

    @Override
    protected CrudService<FormularioOrcamentoDTO> getService() {
        return this.orcamentoService;
    }

    @Override
    protected String getModuloNome() {
        return "TECNICOS";
    }

    @Override
    @GetMapping
    public ResponseEntity<?> listar(@RequestParam Map<String, String> parametros) {
        Map<String, String> params = new HashMap<>(parametros);
        params.put("tecnico", "true");
        return super.listar(params);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTecnico(@PathVariable Long id) {
        if (!SecurityUtil.temPermissao("TECNICOS_VISUALIZAR") && !SecurityUtil.temPermissao("ORCAMENTOS_VISUALIZAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            OrcamentoDTO orcamento = orcamentoService.buscarPorId(id);
            List<ArquivoDTO> arquivos = orcamentoService.listarArquivos(id);

            Map<String, Object> response = new HashMap<>();
            response.put("id", orcamento.getId());
            response.put("nome", orcamento.getNome());
            response.put("bairro", orcamento.getBairro());
            response.put("descricao", orcamento.getDescricao());
            response.put("flagEncerrado", orcamento.getFlagEncerrado());
            response.put("etiquetaId", orcamento.getEtiquetaId());
            response.put("etiquetaNome", orcamento.getEtiquetaNome());
            response.put("pedidoId", orcamento.getPedidoId());
            response.put("pedidoNumero", orcamento.getPedidoNumero());
            response.put("pedidoCliente", orcamento.getPedidoCliente());
            response.put("flagTecnico", orcamento.getFlagTecnico());

            Map<String, String> detalhes = new HashMap<>();
            detalhes.put("Nome", orcamento.getNome());
            detalhes.put("Etiqueta", orcamento.getEtiquetaNome() == null || orcamento.getEtiquetaNome().isBlank() ? "-" : orcamento.getEtiquetaNome());
            detalhes.put("Bairro", orcamento.getBairro() == null || orcamento.getBairro().isBlank() ? "-" : orcamento.getBairro());
            detalhes.put("Descrição", orcamento.getDescricao() == null || orcamento.getDescricao().isBlank() ? "-" : orcamento.getDescricao());
            detalhes.put("Data de Cadastro", orcamento.getDataCadastroFormatado());

            if (orcamento.getPedidoId() != null) {
                try {
                    PedidoDTO pedido = pedidoService.buscarPorId(Long.valueOf(orcamento.getPedidoId()));
                    detalhes.put("Pedido Associado", "#" + pedido.getNumeroPedido() + " - " + pedido.getNomeCliente());
                    response.put("resumoPedido", PedidoViewPresenter.montarResumoPedido(pedido));
                    response.put("dadosCliente", PedidoViewPresenter.montarDadosCliente(pedido));
                    response.put("enderecoObra", PedidoViewPresenter.montarEnderecoObra(pedido, localidadeService));
                    response.put("enderecoCliente", PedidoViewPresenter.montarEnderecoCliente(pedido, localidadeService));
                } catch (Exception ignored) {
                }
            }

            response.put("detalhes", detalhes);
            response.put("arquivos", arquivos);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/arquivos")
    public ResponseEntity<?> enviarArquivo(
            @PathVariable Long id,
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam(value = "etapa", required = false) Integer etapa
    ) {
        if (!SecurityUtil.temPermissao("TECNICOS_EDITAR") && !SecurityUtil.temPermissao("ORCAMENTOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, String> response = new HashMap<>();
        if (arquivo == null || arquivo.isEmpty()) {
            response.put("erro", "Selecione um arquivo para enviar.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            orcamentoService.enviarArquivo(id, arquivo, etapa != null ? etapa : 1);
            response.put("sucesso", "Arquivo enviado com sucesso.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}/arquivos/{arquivoId}")
    public ResponseEntity<?> excluirArquivo(
            @PathVariable Long id,
            @PathVariable Long arquivoId
    ) {
        if (!SecurityUtil.temPermissao("TECNICOS_EDITAR") && !SecurityUtil.temPermissao("ORCAMENTOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, String> response = new HashMap<>();
        try {
            orcamentoService.excluirArquivo(arquivoId);
            response.put("sucesso", "Arquivo excluído com sucesso.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/encerrar")
    public ResponseEntity<?> encerrarTecnico(@PathVariable Long id) {
        if (!SecurityUtil.temPermissao("TECNICOS_EDITAR") && !SecurityUtil.temPermissao("ORCAMENTOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, String> response = new HashMap<>();
        try {
            orcamentoService.encerrarOrcamento(id);
            response.put("sucesso", "Técnico encerrado com sucesso.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/reabrir")
    public ResponseEntity<?> reabrirTecnico(@PathVariable Long id) {
        if (!SecurityUtil.temPermissao("TECNICOS_EDITAR") && !SecurityUtil.temPermissao("ORCAMENTOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, String> response = new HashMap<>();
        try {
            orcamentoService.reabrirOrcamento(id);
            response.put("sucesso", "Técnico reaberto com sucesso.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
