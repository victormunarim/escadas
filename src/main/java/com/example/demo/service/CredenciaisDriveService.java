package com.example.demo.service;

import com.example.demo.dto.CredenciaisDriveDTO;
import com.example.demo.entity.CredenciaisDriveEntity;
import com.example.demo.repository.CredenciaisDriveRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredenciaisDriveService {

    public static final String NOME_PADRAO = "google_drive";

    private final CredenciaisDriveRepository repositorio;

    public CredenciaisDriveService(CredenciaisDriveRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Optional<CredenciaisDriveEntity> obterCredenciais() {
        return repositorio.findById(NOME_PADRAO);
    }

    public Optional<CredenciaisDriveDTO> obterCredenciaisDto() {
        return obterCredenciais()
                .map(cred -> new CredenciaisDriveDTO(
                        cred.getClientId(),
                        cred.getProjectId(),
                        cred.getParentFolder()
                ));
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

        CredenciaisDriveEntity credenciais = repositorio.findById(NOME_PADRAO)
                .orElseGet(() -> new CredenciaisDriveEntity(NOME_PADRAO, "", "", ""));

        credenciais.setClientId(clientIdLimpo);
        credenciais.setProjectId(projectIdLimpo);
        credenciais.setClientSecret(clientSecretLimpo);
        credenciais.setParentFolder(parentFolderLimpo);

        repositorio.save(credenciais);
    }

    private boolean naoVazio(String valor) {
        return valor != null && !valor.isBlank();
    }

    private String limpar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
