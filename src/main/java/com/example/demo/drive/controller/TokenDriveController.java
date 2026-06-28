package com.example.demo.drive.controller;

import com.example.demo.drive.service.OAuthDriveService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TokenDriveController {

    private final OAuthDriveService servicoOAuthDrive;

    public TokenDriveController(OAuthDriveService servicoOAuthDrive) {
        this.servicoOAuthDrive = servicoOAuthDrive;
    }

    @GetMapping("/oauth/google/start")
    public String iniciarOAuth(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            String redirectUri = montarRedirectUri(request);
            String authUrl = servicoOAuthDrive.gerarUrlAutorizacao(redirectUri);
            return "redirect:" + authUrl;
        } catch (Exception e) {
            redirectAttributes.addAttribute("erro", e.getMessage());
            return "redirect:/token";
        }
    }

    @GetMapping("/oauth/google/callback")
    public String callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        if (error != null && !error.isBlank()) {
            redirectAttributes.addAttribute("erro", "Acesso negado: " + error);
            return "redirect:/token";
        }
        if (code == null || code.isBlank()) {
            redirectAttributes.addAttribute("erro", "Código de autorização ausente.");
            return "redirect:/token";
        }

        try {
            String redirectUri = montarRedirectUri(request);
            servicoOAuthDrive.processarCodigo(code, redirectUri);
            redirectAttributes.addAttribute("sucesso", "Token do Google Drive atualizado.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("erro", e.getMessage());
        }

        return "redirect:/token";
    }

    private String montarRedirectUri(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath();

        StringBuilder base = new StringBuilder();
        base.append(scheme).append("://").append(host);
        if (!(scheme.equals("http") && port == 80) && !(scheme.equals("https") && port == 443)) {
            base.append(":").append(port);
        }
        if (contextPath != null && !contextPath.isBlank()) {
            base.append(contextPath);
        }
        base.append("/oauth/google/callback");
        return base.toString();
    }
}