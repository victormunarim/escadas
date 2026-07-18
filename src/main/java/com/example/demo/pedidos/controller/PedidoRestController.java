package com.example.demo.pedidos.controller;

import com.example.demo.auth.security.SecurityUtil;
import com.example.demo.pedidos.config.PedidoViewPresenter;
import com.example.demo.shared.arquivos.dto.ArquivoDTO;
import com.example.demo.pedidos.dto.FormularioPedidoDTO;
import com.example.demo.pedidos.dto.PedidoDTO;
import com.example.demo.shared.crud.controller.AbstractCrudRestController;
import com.example.demo.shared.crud.service.CrudService;
import com.example.demo.pedidos.service.PedidoService;
import com.example.demo.localidades.service.ConsultaLocalidadesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController extends AbstractCrudRestController<FormularioPedidoDTO> {

    private final PedidoService pedidoService;
    private final ConsultaLocalidadesService consultaLocalidadesService;

    public PedidoRestController(
            PedidoService pedidoService,
            ConsultaLocalidadesService consultaLocalidadesService
    ) {
        this.pedidoService = pedidoService;
        this.consultaLocalidadesService = consultaLocalidadesService;
    }

    @Override
    protected CrudService<FormularioPedidoDTO> getService() {
        return this.pedidoService;
    }

    @Override
    protected String getModuloNome() {
        return "PEDIDOS";
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPedido(@PathVariable Long id) {
        if (!SecurityUtil.temPermissao("PEDIDOS_VISUALIZAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            PedidoDTO pedido = pedidoService.buscarPorId(id);
            List<ArquivoDTO> arquivosPedido = pedidoService.listarArquivos(id);

            Map<String, Object> response = new HashMap<>();
            response.put("pedidoId", pedido.getId());
            
            Map<String, String> resumo = new LinkedHashMap<>(PedidoViewPresenter.montarResumoPedido(pedido));
            resumo.entrySet().removeIf(entry -> !SecurityUtil.temAcessoAoCampo(entry.getKey()));
            response.put("resumoPedido", resumo);
            
            response.put("dadosCliente", PedidoViewPresenter.montarDadosCliente(pedido));
            response.put("enderecoObra", PedidoViewPresenter.montarEnderecoObra(pedido, consultaLocalidadesService));
            response.put("enderecoCliente", PedidoViewPresenter.montarEnderecoCliente(pedido, consultaLocalidadesService));
            response.put("arquivosPedido", arquivosPedido);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/arquivos")
    public ResponseEntity<?> enviarArquivo(
            @PathVariable Long id,
            @RequestParam("arquivo") MultipartFile arquivo
    ) {
        if (!SecurityUtil.temPermissao("PEDIDOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, String> response = new HashMap<>();
        if (arquivo == null || arquivo.isEmpty()) {
            response.put("erro", "Selecione um arquivo para enviar.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            pedidoService.enviarArquivo(id, arquivo);
            response.put("sucesso", "Arquivo enviado e vinculado ao pedido.");
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
        if (!SecurityUtil.temPermissao("PEDIDOS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, String> response = new HashMap<>();
        try {
            pedidoService.listarArquivos(id);
            pedidoService.excluirArquivo(arquivoId);
            response.put("sucesso", "Arquivo excluído com sucesso.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
