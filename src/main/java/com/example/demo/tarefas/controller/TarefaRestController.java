package com.example.demo.tarefas.controller;

import com.example.demo.auth.security.SecurityUtil;
import com.example.demo.tarefas.dto.FormularioTarefaDTO;
import com.example.demo.tarefas.dto.TarefaDTO;
import com.example.demo.tarefas.service.TarefaService;
import com.example.demo.shared.crud.controller.AbstractCrudRestController;
import com.example.demo.shared.crud.service.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaRestController extends AbstractCrudRestController<FormularioTarefaDTO> {

    private final TarefaService tarefaService;

    public TarefaRestController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @Override
    protected CrudService<FormularioTarefaDTO> getService() {
        return this.tarefaService;
    }

    @Override
    protected String getModuloNome() {
        return "TAREFAS";
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<?> concluirTarefa(@PathVariable Long id) {
        if (!SecurityUtil.temPermissao("TAREFAS_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            tarefaService.concluirTarefa(id);
            Map<String, String> response = new HashMap<>();
            response.put("sucesso", "Tarefa concluída com sucesso.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/referencia")
    public ResponseEntity<?> listarPorReferencia(
            @RequestParam("extChave") String extChave,
            @RequestParam("extId") Long extId
    ) {
        if (!SecurityUtil.temPermissao("TAREFAS_VISUALIZAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(tarefaService.listarPorReferencia(extChave, extId));
    }
}
