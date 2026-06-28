package com.example.demo.drive.controller;

import com.example.demo.drive.service.CredenciaisDriveService;
import com.example.demo.drive.service.TokenDriveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenRestController {

    private final TokenDriveService servicoTokenDrive;
    private final CredenciaisDriveService servicoCredenciaisDrive;

    public TokenRestController(
            TokenDriveService servicoTokenDrive,
            CredenciaisDriveService servicoCredenciaisDrive
    ) {
        this.servicoTokenDrive = servicoTokenDrive;
        this.servicoCredenciaisDrive = servicoCredenciaisDrive;
    }

    @GetMapping("/api/token/config")
    public ResponseEntity<?> getConfig() {
        Map<String, Object> data = new HashMap<>();
        data.put("tokenConfigurado", servicoTokenDrive.tokenConfigurado());
        data.put("nomeToken", TokenDriveService.NOME_PADRAO);
        data.put("credenciaisConfiguradas", servicoCredenciaisDrive.credenciaisConfiguradas());
        
        servicoCredenciaisDrive.obterCredenciaisDto().ifPresent(cred -> {
            data.put("clientId", cred.getClientId());
            data.put("projectId", cred.getProjectId());
            data.put("parentFolder", cred.getParentFolder());
        });
        
        return ResponseEntity.ok(data);
    }

    @PostMapping("/api/token/credenciais")
    public ResponseEntity<?> atualizarCredenciais(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String clientSecret,
            @RequestParam(required = false) String parentFolder
    ) {
        Map<String, String> response = new HashMap<>();
        try {
            servicoCredenciaisDrive.salvarCredenciais(clientId, projectId, clientSecret, parentFolder);
            response.put("sucesso", "Credenciais do Google Drive atualizadas.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
