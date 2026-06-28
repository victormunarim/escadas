package com.example.demo.pedidos.controller;

import com.example.demo.pedidos.config.PedidoViewPresenter;
import com.example.demo.pedidos.dto.ArquivoPedidoDTO;
import com.example.demo.pedidos.dto.FormularioPedidoDTO;
import com.example.demo.pedidos.dto.PedidoDTO;
import com.example.demo.shared.crud.controller.AbstractCrudRestController;
import com.example.demo.shared.crud.service.CrudService;
import com.example.demo.pedidos.service.PedidoService;
import com.example.demo.localidades.service.ConsultaLocalidadesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPedido(@PathVariable Long id) {
        try {
            PedidoDTO pedido = pedidoService.buscarPorId(id);
            List<ArquivoPedidoDTO> arquivosPedido = pedidoService.listarArquivos(id);

            Map<String, Object> response = new HashMap<>();
            response.put("pedidoId", pedido.getId());
            response.put("resumoPedido", PedidoViewPresenter.montarResumoPedido(pedido));
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
}
