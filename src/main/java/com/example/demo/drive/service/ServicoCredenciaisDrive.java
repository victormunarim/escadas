package com.example.demo.drive.service;

import com.example.demo.drive.model.CredenciaisDrive;
import com.example.demo.drive.repository.RepositorioCredenciaisDrive;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicoCredenciaisDrive {

    public static final String NOME_PADRAO = "google_drive";

    private final RepositorioCredenciaisDrive repositorio;

    public ServicoCredenciaisDrive(RepositorioCredenciaisDrive repositorio) {
        this.repositorio = repositorio;
    }

    public Optional<CredenciaisDrive> obterCredenciais() {
        return repositorio.findById(NOME_PADRAO);
    }

    public boolean credenciaisConfiguradas() {
        return obterCredenciais()
                .filter(cred -> naoVazio(cred.getClientId()))
                .filter(cred -> naoVazio(cred.getProjectId()))
                .filter(cred -> naoVazio(cred.getClientSecret()))
                .filter(cred -> naoVazio(cred.getParentFolder()))
                .isPresent();
    }

    public void salvarCredenciais(String clientId, String projectId, String clientSecret, String parentFolder) {
        String clientIdLimpo = limpar(clientId);
        String projectIdLimpo = limpar(projectId);
        String clientSecretLimpo = limpar(clientSecret);
        String parentFolderLimpo = limpar(parentFolder);

        CredenciaisDrive credenciais = repositorio.findById(NOME_PADRAO)
                .orElseGet(() -> new CredenciaisDrive(NOME_PADRAO, "", "", ""));

        if (!naoVazio(clientIdLimpo)) {
            throw new IllegalStateException("client_id é obrigatório.");
        }
        if (!naoVazio(projectIdLimpo)) {
            throw new IllegalStateException("project_id é obrigatório.");
        }
        if (!naoVazio(parentFolderLimpo)) {
            throw new IllegalStateException("parent_folder é obrigatório.");
        }

        credenciais.setClientId(clientIdLimpo);
        credenciais.setProjectId(projectIdLimpo);
        credenciais.setParentFolder(parentFolderLimpo);

        if (naoVazio(clientSecretLimpo)) {
            credenciais.setClientSecret(clientSecretLimpo);
        } else if (!naoVazio(credenciais.getClientSecret())) {
            throw new IllegalStateException("client_secret é obrigatório.");
        }

        repositorio.save(credenciais);
    }

    private boolean naoVazio(String valor) {
        return valor != null && !valor.isBlank();
    }

    private String limpar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
