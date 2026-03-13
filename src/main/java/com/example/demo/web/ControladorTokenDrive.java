package com.example.demo.web;

import com.example.demo.drive.service.ServicoOAuthDrive;
import com.example.demo.drive.service.ServicoCredenciaisDrive;
import com.example.demo.drive.service.ServicoTokenDrive;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControladorTokenDrive {

    private final ServicoOAuthDrive servicoOAuthDrive;
    private final ServicoTokenDrive servicoTokenDrive;
    private final ServicoCredenciaisDrive servicoCredenciaisDrive;

    public ControladorTokenDrive(
            ServicoOAuthDrive servicoOAuthDrive,
            ServicoTokenDrive servicoTokenDrive,
            ServicoCredenciaisDrive servicoCredenciaisDrive
    ) {
        this.servicoOAuthDrive = servicoOAuthDrive;
        this.servicoTokenDrive = servicoTokenDrive;
        this.servicoCredenciaisDrive = servicoCredenciaisDrive;
    }

    @GetMapping("/token")
    public String token(
            @RequestParam(required = false) String sucesso,
            @RequestParam(required = false) String erro,
            Model model
    ) {
        model.addAttribute("tokenConfigurado", servicoTokenDrive.tokenConfigurado());
        model.addAttribute("nomeToken", ServicoTokenDrive.NOME_PADRAO);
        model.addAttribute("credenciaisConfiguradas", servicoCredenciaisDrive.credenciaisConfiguradas());
        servicoCredenciaisDrive.obterCredenciais().ifPresent(cred -> {
            model.addAttribute("clientId", cred.getClientId());
            model.addAttribute("projectId", cred.getProjectId());
            model.addAttribute("parentFolder", cred.getParentFolder());
        });
        model.addAttribute("sucesso", sucesso);
        model.addAttribute("erro", erro);
        return "token";
    }

    @PostMapping("/token/credenciais")
    public String atualizarCredenciais(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String clientSecret,
            @RequestParam(required = false) String parentFolder,
            RedirectAttributes redirectAttributes
    ) {
        try {
            servicoCredenciaisDrive.salvarCredenciais(clientId, projectId, clientSecret, parentFolder);
            redirectAttributes.addAttribute("sucesso", "Credenciais do Google Drive atualizadas.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("erro", e.getMessage());
        }
        return "redirect:/token";
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
