package com.example.demo.service;

import com.example.demo.entity.CredenciaisDriveEntity;
import com.example.demo.repository.CredenciaisDriveRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenDriveService {

    public static final String NOME_PADRAO = "google_drive";

    private final CredenciaisDriveRepository repositorio;

    public TokenDriveService(CredenciaisDriveRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Optional<String> obterRefreshToken() {
        return repositorio.findById(NOME_PADRAO).map(CredenciaisDriveEntity::getToken);
    }

    public boolean tokenConfigurado() {
        return obterRefreshToken().filter(token -> !token.isBlank()).isPresent();
    }

    public void salvarToken(String refreshToken) {
        CredenciaisDriveEntity credenciais = repositorio.findById(NOME_PADRAO)
                .orElseThrow(() -> new IllegalStateException(
                        "Credenciais do Google Drive não configuradas."
                ));
        credenciais.setToken(refreshToken);
        repositorio.save(credenciais);
    }
}
