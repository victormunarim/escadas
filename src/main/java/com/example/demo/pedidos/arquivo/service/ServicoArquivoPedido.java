package com.example.demo.pedidos.arquivo.service;

import com.example.demo.drive.service.ServicoTokenDrive;
import com.example.demo.drive.service.ServicoCredenciaisDrive;
import com.example.demo.drive.model.CredenciaisDrive;
import com.example.demo.pedidos.arquivo.model.ArquivoPedido;
import com.example.demo.pedidos.arquivo.repository.RepositorioArquivoPedido;
import com.example.demo.pedidos.model.Pedido;
import com.example.demo.util.FormatacaoUtil;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoArquivoPedido {

    private final RepositorioArquivoPedido repositorioArquivoPedido;
    private final ServicoTokenDrive servicoTokenDrive;
    private final ServicoCredenciaisDrive servicoCredenciaisDrive;

    @Value("${google.drive.parent-folder-id:}")
    private String pastaPaiId;

    public ServicoArquivoPedido(
            RepositorioArquivoPedido repositorioArquivoPedido,
            ServicoTokenDrive servicoTokenDrive,
            ServicoCredenciaisDrive servicoCredenciaisDrive
    ) {
        this.repositorioArquivoPedido = repositorioArquivoPedido;
        this.servicoTokenDrive = servicoTokenDrive;
        this.servicoCredenciaisDrive = servicoCredenciaisDrive;
    }

    public List<ArquivoPedido> listarPorPedido(Long pedidoId) {
        return repositorioArquivoPedido.findByIdPedidoIdOrderByIdNomeAsc(pedidoId);
    }

    @Transactional
    public ArquivoPedido enviarERegistrar(Pedido pedido, MultipartFile multipartFile) {
        validarConfiguracao();

        String nomeArquivo = nomeArquivo(multipartFile);

        try {
            Drive drive = clienteDrive();
            String pastaPedidoId = obterOuCriarPastaPedido(drive, pedido);
            String linkArquivo = enviarArquivoParaDrive(drive, pastaPedidoId, nomeArquivo, multipartFile);

            ArquivoPedido arquivoPedido = new ArquivoPedido(nomeArquivo, pedido.getId(), linkArquivo);
            return repositorioArquivoPedido.save(arquivoPedido);
        } catch (GoogleJsonResponseException e) {
            throw traduzirErroGoogleDrive(e);
        } catch (IOException | GeneralSecurityException e) {
            String detalhe = e.getMessage() == null ? "sem detalhe adicional" : e.getMessage();
            throw new IllegalStateException("Falha ao enviar arquivo para o Google Drive: " + detalhe, e);
        }
    }

    private String enviarArquivoParaDrive(
            Drive drive,
            String pastaPedidoId,
            String nomeArquivo,
            MultipartFile multipartFile
    ) throws IOException {
        File metadata = new File();
        metadata.setName(nomeArquivo);
        metadata.setParents(Collections.singletonList(pastaPedidoId));

        InputStreamContent mediaContent =
                new InputStreamContent(multipartFile.getContentType(), multipartFile.getInputStream());
        mediaContent.setLength(multipartFile.getSize());

        File arquivoCriado = drive.files()
                .create(metadata, mediaContent)
                .setSupportsAllDrives(true)
                .setFields("id,webViewLink,webContentLink")
                .execute();

        Permission permissaoPublica = new Permission();
        permissaoPublica.setType("anyone");
        permissaoPublica.setRole("reader");
        drive.permissions()
                .create(arquivoCriado.getId(), permissaoPublica)
                .setSupportsAllDrives(true)
                .execute();

        if (arquivoCriado.getWebViewLink() != null && !arquivoCriado.getWebViewLink().isBlank()) {
            return arquivoCriado.getWebViewLink();
        }

        return "https://drive.google.com/file/d/" + arquivoCriado.getId() + "/view";
    }

    private String obterOuCriarPastaPedido(Drive drive, Pedido pedido) throws IOException {
        String nomePasta = FormatacaoUtil.nomePastaPedido(pedido.getNomeCliente(), pedido.getNumeroPedido());

        Optional<String> pastaExistente = buscarPastaPedido(drive, nomePasta);
        if (pastaExistente.isPresent()) {
            return pastaExistente.get();
        }

        File pastaMetadata = new File();
        pastaMetadata.setName(nomePasta);
        pastaMetadata.setMimeType("application/vnd.google-apps.folder");

        if (pastaPaiConfigurada()) {
            pastaMetadata.setParents(Collections.singletonList(pastaPaiId.trim()));
        }

        File pastaCriada = drive.files()
                .create(pastaMetadata)
                .setSupportsAllDrives(true)
                .setFields("id")
                .execute();

        return pastaCriada.getId();
    }

    private Optional<String> buscarPastaPedido(Drive drive, String nomePasta) throws IOException {
        StringBuilder consulta = new StringBuilder();
        consulta.append("mimeType = 'application/vnd.google-apps.folder' ");
        consulta.append("and trashed = false ");
        consulta.append("and name = '").append(nomePasta.replace("'", "\\'")).append("'");

        if (pastaPaiConfigurada()) {
            consulta.append(" and '").append(pastaPaiId.trim()).append("' in parents");
        }

        FileList resposta = drive.files()
                .list()
                .setQ(consulta.toString())
                .setPageSize(1)
                .setIncludeItemsFromAllDrives(true)
                .setSupportsAllDrives(true)
                .setFields("files(id,name)")
                .execute();

        if (resposta.getFiles() == null || resposta.getFiles().isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(resposta.getFiles().getFirst().getId());
    }

    private Drive clienteDrive() throws IOException, GeneralSecurityException {
        String refreshToken = obterRefreshTokenObrigatorio();
        CredenciaisDrive credenciaisDrive = obterCredenciaisObrigatorias();
        GoogleCredentials credenciais = UserCredentials.newBuilder()
                .setClientId(credenciaisDrive.getClientId())
                .setClientSecret(credenciaisDrive.getClientSecret())
                .setRefreshToken(refreshToken)
                .build()
                .createScoped(List.of(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credenciais)
        ).setApplicationName("escadas").build();
    }

    private String nomeArquivo(MultipartFile multipartFile) {
        String original = multipartFile.getOriginalFilename();
        if (original == null || original.isBlank()) {
            return "arquivo";
        }
        return Path.of(original).getFileName().toString();
    }

    private boolean pastaPaiConfigurada() {
        return pastaPaiId != null && !pastaPaiId.isBlank();
    }

    private void validarConfiguracao() {
        List<String> erros = new ArrayList<>();

        String refreshToken = servicoTokenDrive.obterRefreshToken().orElse(null);
        if (refreshToken == null || refreshToken.isBlank()) {
            erros.add("Token OAuth do Google Drive não configurado. Use o módulo Google Drive.");
        }

        if (!servicoCredenciaisDrive.credenciaisConfiguradas()) {
            erros.add("Credenciais do Google Drive não configuradas. Preencha client_id e client_secret.");
        }

        if (pastaPaiId == null || pastaPaiId.isBlank()) {
            erros.add("Configure google.drive.parent-folder-id com a pasta de um Shared Drive.");
        }

        if (!erros.isEmpty()) {
            throw new IllegalStateException(String.join(" ", erros));
        }
    }

    private String obterRefreshTokenObrigatorio() {
        String refreshToken = servicoTokenDrive.obterRefreshToken().orElse(null);
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalStateException(
                    "Token OAuth do Google Drive não configurado. Use o módulo Google Drive."
            );
        }
        return refreshToken;
    }

    private CredenciaisDrive obterCredenciaisObrigatorias() {
        return servicoCredenciaisDrive.obterCredenciais()
                .filter(cred -> cred.getClientId() != null && !cred.getClientId().isBlank())
                .filter(cred -> cred.getClientSecret() != null && !cred.getClientSecret().isBlank())
                .orElseThrow(() -> new IllegalStateException(
                        "Credenciais do Google Drive não configuradas. Preencha client_id e client_secret."
                ));
    }

    private IllegalStateException traduzirErroGoogleDrive(GoogleJsonResponseException e) {
        int status = e.getStatusCode();
        String mensagem = e.getDetails() != null && e.getDetails().getMessage() != null
                ? e.getDetails().getMessage()
                : e.getMessage();

        if (status == 403 && mensagem != null && mensagem.contains("storageQuotaExceeded")) {
            return new IllegalStateException(
                    "Google Drive recusou o upload por quota. Use Shared Drive e configure google.drive.parent-folder-id com uma pasta desse Shared Drive.",
                    e
            );
        }

        return new IllegalStateException("Falha ao enviar arquivo para o Google Drive: " + mensagem, e);
    }
}
