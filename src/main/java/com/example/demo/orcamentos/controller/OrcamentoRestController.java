package com.example.demo.orcamentos.controller;

import com.example.demo.auth.security.SecurityUtil;
import com.example.demo.localidades.service.ConsultaLocalidadesService;
import com.example.demo.orcamentos.config.OrcamentoViewPresenter;
import com.example.demo.orcamentos.dto.FormularioOrcamentoDTO;
import com.example.demo.orcamentos.dto.OrcamentoDTO;
import com.example.demo.orcamentos.service.OrcamentoService;
import com.example.demo.pedidos.service.PedidoService;
import com.example.demo.shared.arquivos.dto.ArquivoDTO;
import com.example.demo.shared.crud.controller.AbstractCrudRestController;
import com.example.demo.shared.crud.service.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoRestController extends AbstractCrudRestController<FormularioOrcamentoDTO> {

    private final OrcamentoService orcamentoService;
    private final PedidoService pedidoService;
    private final ConsultaLocalidadesService localidadeService;

    public OrcamentoRestController(
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
        return "ORCAMENTOS";
    }

    @Override
    @GetMapping
    public ResponseEntity<?> listar(@RequestParam Map<String, String> parametros) {
        Map<String, String> params = new HashMap<>(parametros);
        params.put("tecnico", "false");
        return super.listar(params);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarOrcamento(@PathVariable Long id) {
        if (SecurityUtil.naoTemPermissao("ORCAMENTOS_VISUALIZAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            OrcamentoDTO orcamento = orcamentoService.buscarPorId(id);
            List<ArquivoDTO> arquivos = orcamentoService.listarArquivos(id);
            Map<String, Object> response = OrcamentoViewPresenter.montarVisualizacao(orcamento, arquivos, pedidoService, localidadeService);
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
        if (SecurityUtil.naoTemPermissao("ORCAMENTOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (arquivo == null || arquivo.isEmpty()) {
            return respostaErro("Selecione um arquivo para enviar.");
        }

        try {
            orcamentoService.enviarArquivo(id, arquivo, etapa != null ? etapa : 1);
            return respostaSucesso("Arquivo enviado e vinculado ao orçamento.");
        } catch (Exception e) {
            return respostaErro(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/arquivos/{arquivoId}")
    public ResponseEntity<?> excluirArquivo(
            @PathVariable Long id,
            @PathVariable Long arquivoId
    ) {
        if (SecurityUtil.naoTemPermissao("ORCAMENTOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            orcamentoService.excluirArquivo(arquivoId);
            return respostaSucesso("Arquivo excluído com sucesso.");
        } catch (Exception e) {
            return respostaErro(e.getMessage());
        }
    }

    @PutMapping("/{id}/encerrar")
    public ResponseEntity<?> encerrarOrcamento(@PathVariable Long id) {
        if (SecurityUtil.naoTemPermissao("ORCAMENTOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            orcamentoService.encerrarOrcamento(id);
            return respostaSucesso("Orçamento encerrado com sucesso.");
        } catch (Exception e) {
            return respostaErro(e.getMessage());
        }
    }

    @PutMapping("/{id}/reabrir")
    public ResponseEntity<?> reabrirOrcamento(@PathVariable Long id) {
        if (SecurityUtil.naoTemPermissao("ORCAMENTOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            orcamentoService.reabrirOrcamento(id);
            return respostaSucesso("Orçamento reaberto com sucesso.");
        } catch (Exception e) {
            return respostaErro(e.getMessage());
        }
    }
}
