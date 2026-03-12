package com.example.demo.dev;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GeradorTokenDrive {

    private static final List<String> SCOPES = List.of(DriveScopes.DRIVE);
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String AUTH_URI = "https://accounts.google.com/o/oauth2/auth";
    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";

    public static void main(String[] args) throws Exception {
        String caminhoToken = argumento(args, "--token", "./token.json");
        String clientId = argumentoObrigatorio(args, "--client-id");
        String projectId = argumentoObrigatorio(args, "--project-id");
        String clientSecret = argumentoObrigatorio(args, "--client-secret");
        int porta = Integer.parseInt(argumento(args, "--port", "8888"));

        GoogleClientSecrets secrets = montarCredenciais(clientId, projectId, clientSecret);
        GoogleClientSecrets.Details details = secrets.getInstalled();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(porta).build();
        String redirectUri = receiver.getRedirectUri();

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                secrets,
                SCOPES
        ).setAccessType("offline").build();

        GoogleAuthorizationCodeRequestUrl authUrl = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setAccessType("offline");
        authUrl.set("prompt", "consent");

        System.out.println("Abra a URL abaixo no navegador e conclua o login:");
        System.out.println(authUrl.build());

        String code;
        try {
            code = receiver.waitForCode();
        } finally {
            receiver.stop();
        }

        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
        String refreshToken = response.getRefreshToken();

        if (refreshToken == null || refreshToken.isBlank()) {
            System.err.println("ERRO: não foi possível obter refresh_token.");
            System.err.println("Tente novamente garantindo prompt=consent e acesso offline.");
            System.exit(2);
        }

        Map<String, Object> tokenJson = new LinkedHashMap<>();
        tokenJson.put("type", "authorized_user");
        tokenJson.put("client_id", details.getClientId());
        tokenJson.put("client_secret", details.getClientSecret());
        tokenJson.put("refresh_token", refreshToken);
        tokenJson.put("token_uri", details.getTokenUri());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(tokenJson));
    }

    private static GoogleClientSecrets montarCredenciais(String clientId, String projectId, String clientSecret) {
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAuthUri(AUTH_URI);
        details.setTokenUri(TOKEN_URI);

        GoogleClientSecrets secrets = new GoogleClientSecrets();
        secrets.setInstalled(details);
        return secrets;
    }

    private static String argumentoObrigatorio(String[] args, String nome) {
        String valor = argumento(args, nome, null);
        if (valor == null || valor.isBlank()) {
            System.err.println("ERRO: argumento obrigatório ausente: " + nome);
            System.exit(1);
        }
        return valor;
    }

    private static String argumento(String[] args, String nome, String padrao) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(nome) && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        return padrao;
    }
}
