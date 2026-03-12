package com.example.demo.drive.service;

import com.example.demo.drive.model.CredenciaisDrive;
import com.example.demo.drive.repository.RepositorioCredenciaisDrive;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicoTokenDrive {

    public static final String NOME_PADRAO = "google_drive";

    private final RepositorioCredenciaisDrive repositorio;

    public ServicoTokenDrive(RepositorioCredenciaisDrive repositorio) {
        this.repositorio = repositorio;
    }

    public Optional<String> obterRefreshToken() {
        return repositorio.findById(NOME_PADRAO).map(CredenciaisDrive::getToken);
    }

    public boolean tokenConfigurado() {
        return obterRefreshToken().filter(token -> !token.isBlank()).isPresent();
    }

    public void salvarToken(String refreshToken) {
        CredenciaisDrive credenciais = repositorio.findById(NOME_PADRAO)
                .orElseThrow(() -> new IllegalStateException(
                        "Credenciais do Google Drive não configuradas."
                ));
        credenciais.setToken(refreshToken);
        repositorio.save(credenciais);
    }
}
