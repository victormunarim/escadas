package com.example.demo.shared.crud.controller;

import com.example.demo.auth.security.SecurityUtil;
import com.example.demo.shared.crud.render.*;
import com.example.demo.shared.crud.service.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCrudRestController<TFormDTO> {

    protected abstract CrudService<TFormDTO> getService();
    
    /**
     * Retorna o nome do módulo associado ao controller para verificação de permissões.
     * Ex: "PEDIDOS", "ORCAMENTOS"
     */
    protected abstract String getModuloNome();

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam Map<String, String> parametros) {
        if (!SecurityUtil.temPermissao(getModuloNome() + "_VISUALIZAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, String> parametrosEfetivos = new HashMap<>(parametros);
        parametrosEfetivos.putIfAbsent("size", "50");
        ListagemDTO listagem = getService().listarResumo(parametrosEfetivos);
        return ResponseEntity.ok(SecurityUtil.filtrarListagem(listagem));
    }

    @GetMapping("/formulario")
    public ResponseEntity<?> obterFormularioNovo() {
        if (!SecurityUtil.temPermissao(getModuloNome() + "_ADICIONAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<CampoRender> campos = getService().obterCamposRenderNovo();
        return ResponseEntity.ok(SecurityUtil.filtrarCampos(campos));
    }

    @GetMapping("/{id}/formulario")
    public ResponseEntity<?> obterFormularioEdicao(@PathVariable Long id) {
        if (!SecurityUtil.temPermissao(getModuloNome() + "_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<CampoRender> campos = getService().obterCamposRenderEdicao(id);
            return ResponseEntity.ok(SecurityUtil.filtrarCampos(campos));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody TFormDTO formulario) {
        if (!SecurityUtil.temPermissao(getModuloNome() + "_ADICIONAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
        if (!SecurityUtil.temPermissao(getModuloNome() + "_EDITAR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
        if (!SecurityUtil.temPermissao(getModuloNome() + "_EXCLUIR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
