package com.example.demo.shared.crud.controller;

import com.example.demo.shared.crud.render.*;
import com.example.demo.shared.crud.service.CrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCrudRestController<TFormDTO> {

    protected abstract CrudService<TFormDTO> getService();

    @GetMapping
    public ResponseEntity<ListagemDTO> listar(@RequestParam Map<String, String> parametros) {
        Map<String, String> parametrosEfetivos = new HashMap<>(parametros);
        parametrosEfetivos.putIfAbsent("size", "50");
        return ResponseEntity.ok(getService().listarResumo(parametrosEfetivos));
    }

    @GetMapping("/formulario")
    public ResponseEntity<List<CampoRender>> obterFormularioNovo() {
        return ResponseEntity.ok(getService().obterCamposRenderNovo());
    }

    @GetMapping("/{id}/formulario")
    public ResponseEntity<List<CampoRender>> obterFormularioEdicao(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(getService().obterCamposRenderEdicao(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody TFormDTO formulario) {
        try {
            Long id = getService().salvarFormulario(formulario);
            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", "Registro cadastrado com sucesso.");
            response.put("id", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody TFormDTO formulario) {
        try {
            getService().atualizarFormulario(id, formulario);
            Map<String, String> response = new HashMap<>();
            response.put("sucesso", "Registro atualizado com sucesso.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            getService().excluir(id);
            Map<String, String> response = new HashMap<>();
            response.put("sucesso", "Registro excluído com sucesso.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
