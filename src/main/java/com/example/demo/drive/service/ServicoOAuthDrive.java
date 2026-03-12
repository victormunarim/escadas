package com.example.demo.drive.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Value;
import com.google.api.services.drive.DriveScopes;
import com.google.api.client.auth.oauth2.TokenResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class ServicoOAuthDrive {

    private static final List<String> SCOPES = List.of(DriveScopes.DRIVE);
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String AUTH_URI = "https://accounts.google.com/o/oauth2/auth";
    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";

    private final ServicoTokenDrive servicoTokenDrive;
    private final ServicoCredenciaisDrive servicoCredenciaisDrive;

    @Value("${google.drive.oauth-redirect-uri:}")
    private String redirectUriConfigurado;

    public ServicoOAuthDrive(
            ServicoTokenDrive servicoTokenDrive,
            ServicoCredenciaisDrive servicoCredenciaisDrive
    ) {
        this.servicoTokenDrive = servicoTokenDrive;
        this.servicoCredenciaisDrive = servicoCredenciaisDrive;
    }

    public String gerarUrlAutorizacao(String redirectUriDinamico)
            throws IOException, GeneralSecurityException {
        GoogleClientSecrets secrets = carregarCredenciais();
        GoogleClientSecrets.Details details = obterDetalhes(secrets);
        String redirectUri = escolherRedirectUri(details, redirectUriDinamico);

        GoogleAuthorizationCodeFlow flow = novoFluxo(secrets);
        GoogleAuthorizationCodeRequestUrl authUrl = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setAccessType("offline");
        authUrl.set("prompt", "consent");

        return authUrl.build();
    }

    public void processarCodigo(String code, String redirectUriDinamico)
            throws IOException, GeneralSecurityException {
        GoogleClientSecrets secrets = carregarCredenciais();
        GoogleClientSecrets.Details details = obterDetalhes(secrets);
        String redirectUri = escolherRedirectUri(details, redirectUriDinamico);

        GoogleAuthorizationCodeFlow flow = novoFluxo(secrets);
        TokenResponse response = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();

        String refreshToken = response.getRefreshToken();
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalStateException(
                    "Não foi possível obter refresh_token. Tente novamente com consentimento."
            );
        }

        servicoTokenDrive.salvarToken(refreshToken);
    }

    private GoogleAuthorizationCodeFlow novoFluxo(GoogleClientSecrets secrets)
            throws IOException, GeneralSecurityException {
        return new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                secrets,
                SCOPES
        ).setAccessType("offline").build();
    }

    private GoogleClientSecrets carregarCredenciais() throws IOException {
        return carregarCredenciaisDoFormulario();
    }

    private String escolherRedirectUri(GoogleClientSecrets.Details details, String redirectUriDinamico) {
        String redirectUri = (redirectUriConfigurado == null || redirectUriConfigurado.isBlank())
                ? redirectUriDinamico
                : redirectUriConfigurado.trim();

        if (details.getRedirectUris() != null && !details.getRedirectUris().isEmpty()) {
            if (!details.getRedirectUris().contains(redirectUri)) {
                throw new IllegalStateException(
                        "Redirect URI não autorizado no Google Cloud: " + redirectUri
                );
            }
        }

        return redirectUri;
    }

    private GoogleClientSecrets carregarCredenciaisDoFormulario() {
        return servicoCredenciaisDrive.obterCredenciais()
                .filter(cred -> cred.getClientId() != null && !cred.getClientId().isBlank())
                .filter(cred -> cred.getProjectId() != null && !cred.getProjectId().isBlank())
                .filter(cred -> cred.getClientSecret() != null && !cred.getClientSecret().isBlank())
                .map(cred -> {
                    GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
                    details.setClientId(cred.getClientId().trim());
                    details.setClientSecret(cred.getClientSecret().trim());
                    details.setAuthUri(AUTH_URI);
                    details.setTokenUri(TOKEN_URI);

                    GoogleClientSecrets secrets = new GoogleClientSecrets();
                    secrets.setInstalled(details);
                    return secrets;
                })
                .orElseThrow(() -> new IllegalStateException(
                        "Credenciais do Google Drive não configuradas. Preencha client_id, project_id e client_secret."
                ));
    }

    private GoogleClientSecrets.Details obterDetalhes(GoogleClientSecrets secrets) {
        GoogleClientSecrets.Details details = secrets.getInstalled();
        if (details == null) {
            details = secrets.getWeb();
        }
        if (details == null) {
            throw new IllegalStateException("credentials.json inválido. Esperado 'installed' ou 'web'.");
        }
        return details;
    }
}
