package com.example.demo.shared.arquivos.service;

import com.example.demo.shared.arquivos.model.ArquivoEntity;
import com.example.demo.shared.arquivos.repository.ArquivoRepository;
import com.example.demo.shared.arquivos.dto.ArquivoDTO;
import com.example.demo.drive.model.CredenciaisDriveEntity;
import com.example.demo.drive.service.CredenciaisDriveService;
import com.example.demo.drive.service.TokenDriveService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
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
public class ArquivoService {

    private final ArquivoRepository repositorioArquivo;
    private final TokenDriveService servicoTokenDrive;
    private final CredenciaisDriveService servicoCredenciaisDrive;

    public ArquivoService(
            ArquivoRepository repositorioArquivo,
            TokenDriveService servicoTokenDrive,
            CredenciaisDriveService servicoCredenciaisDrive
    ) {
        this.repositorioArquivo = repositorioArquivo;
        this.servicoTokenDrive = servicoTokenDrive;
        this.servicoCredenciaisDrive = servicoCredenciaisDrive;
    }

    public List<ArquivoDTO> listar(String extChave, Long extId) {
        return repositorioArquivo.findByExtChaveAndExtIdOrderByNomeAsc(extChave, extId).stream()
                .map(ArquivoDTO::new)
                .toList();
    }

    @Transactional
    public void enviarERegistrar(String extChave, Long extId, String nomePasta, MultipartFile multipartFile) {
        enviarERegistrar(extChave, extId, nomePasta, multipartFile, null);
    }

    @Transactional
    public void enviarERegistrar(
            String extChave, Long extId, String nomePasta, MultipartFile multipartFile, Integer etapa
    ) {
        validarConfiguracao();

        String pastaPaiId = obterPastaPaiObrigatoria();
        String nomeArquivo = nomeArquivo(multipartFile);

        try {
            Drive drive = clienteDrive();
            
            // 1. Obter ou criar a pasta correspondente à entidade (Orçamentos ou Pedidos) dentro do root
            String pastaEntidadeTipo = "pedido_id".equals(extChave) ? "Pedidos" : "Orçamentos";
            String pastaEntidadeTipoId = obterOuCriarPasta(drive, pastaEntidadeTipo, pastaPaiId);
            
            // 2. Obter ou criar a pasta do registro (e.g. Orcamento - Nome [ID]) dentro da pasta da entidade tipo
            String pastaRegistroId = obterOuCriarPasta(drive, nomePasta, pastaEntidadeTipoId);
            
            // 3. Obter ou criar a pasta da etapa (e.g. Etapa X) se houver etapa
            String pastaFinalId = pastaRegistroId;
            if (etapa != null) {
                String nomePastaEtapa = "Etapa " + etapa;
                pastaFinalId = obterOuCriarPasta(drive, nomePastaEtapa, pastaRegistroId);
            }

            String linkArquivo = enviarArquivoParaDrive(drive, pastaFinalId, nomeArquivo, multipartFile);

            ArquivoEntity arquivo = new ArquivoEntity();
            arquivo.setNome(nomeArquivo);
            arquivo.setExtChave(extChave);
            arquivo.setExtId(extId);
            arquivo.setLink(linkArquivo);
            arquivo.setEtapa(etapa);

            repositorioArquivo.save(arquivo);
        } catch (GoogleJsonResponseException e) {
            throw traduzirErroGoogleDrive(e);
        } catch (IOException | GeneralSecurityException e) {
            String detalhe = e.getMessage() == null ? "sem detalhe adicional" : e.getMessage();
            if (detalhe.contains("invalid_grant") || detalhe.contains("revoked") || detalhe.contains("expired")) {
                try {
                    servicoTokenDrive.salvarToken("");
                } catch (Exception ex) {
                    System.err.println("Erro ao limpar token inválido: " + ex.getMessage());
                }
            }
            throw new IllegalStateException("Falha ao enviar arquivo para o Google Drive: " + detalhe, e);
        }
    }

    @Transactional
    public void excluir(Long arquivoId) {
        ArquivoEntity arquivo = repositorioArquivo.findById(arquivoId)
                .orElseThrow(() -> new IllegalArgumentException("Arquivo não encontrado."));

        try {
            String link = arquivo.getLink();
            if (link != null && link.contains("/d/")) {
                String fileId = link.split("/d/")[1].split("/")[0];
                Drive drive = clienteDrive();
                drive.files().delete(fileId).setSupportsAllDrives(true).execute();
            }
        } catch (Exception e) {
            System.err.println("Erro ao deletar arquivo do Google Drive: " + e.getMessage());
        }

        repositorioArquivo.delete(arquivo);
    }

    private String enviarArquivoParaDrive(
            Drive drive,
            String pastaDestinoId,
            String nomeArquivo,
            MultipartFile multipartFile
    ) throws IOException {
        File metadata = new File();
        metadata.setName(nomeArquivo);
        metadata.setParents(Collections.singletonList(pastaDestinoId));

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

    private String obterOuCriarPasta(Drive drive, String nomePasta, String pastaPaiId) throws IOException {
        Optional<String> pastaExistente = buscarPasta(drive, nomePasta, pastaPaiId);
        if (pastaExistente.isPresent()) {
            return pastaExistente.get();
        }

        File pastaMetadata = new File();
        pastaMetadata.setName(nomePasta);
        pastaMetadata.setMimeType("application/vnd.google-apps.folder");
        pastaMetadata.setParents(Collections.singletonList(pastaPaiId.trim()));

        File pastaCriada = drive.files()
                .create(pastaMetadata)
                .setSupportsAllDrives(true)
                .setFields("id")
                .execute();

        return pastaCriada.getId();
    }

    private Optional<String> buscarPasta(Drive drive, String nomePasta, String pastaPaiId) throws IOException {
        String consulta = "mimeType = 'application/vnd.google-apps.folder' " +
                "and trashed = false " +
                "and name = '" + nomePasta.replace("'", "\\'") + "'" +
                " and '" + pastaPaiId.trim() + "' in parents";

        FileList resposta = drive.files()
                .list()
                .setQ(consulta)
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
        CredenciaisDriveEntity credenciaisDrive = obterCredenciaisObrigatorias();
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

    private void validarConfiguracao() {
        List<String> erros = new ArrayList<>();

        String refreshToken = servicoTokenDrive.obterRefreshToken().orElse(null);
        if (refreshToken == null || refreshToken.isBlank()) {
            erros.add("Token OAuth do Google Drive não configurado. Use o módulo Google Drive.");
        }

        if (!servicoCredenciaisDrive.credenciaisConfiguradas()) {
            erros.add("Credenciais do Google Drive não configuradas. "
                    + "Preencha client_id, project_id, client_secret e parent_folder.");
        }

        String pastaPai = servicoCredenciaisDrive.obterCredenciais()
                .map(CredenciaisDriveEntity::getParentFolder)
                .orElse(null);
        if (pastaPai == null || pastaPai.isBlank()) {
            erros.add("Pasta pai do Google Drive não configurada. Informe a pasta no módulo Google Drive.");
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

    private CredenciaisDriveEntity obterCredenciaisObrigatorias() {
        return servicoCredenciaisDrive.obterCredenciais()
                .filter(cred -> cred.getClientId() != null && !cred.getClientId().isBlank())
                .filter(cred -> cred.getClientSecret() != null && !cred.getClientSecret().isBlank())
                .orElseThrow(() -> new IllegalStateException(
                        "Credenciais do Google Drive não configuradas. Preencha client_id e client_secret."
                ));
    }

    private String obterPastaPaiObrigatoria() {
        return servicoCredenciaisDrive.obterCredenciais()
                .map(CredenciaisDriveEntity::getParentFolder)
                .filter(valor -> !valor.isBlank())
                .map(String::trim)
                .orElseThrow(() -> new IllegalStateException(
                        "Pasta pai do Google Drive não configurada. Informe a pasta no módulo Google Drive."
                ));
    }

    private IllegalStateException traduzirErroGoogleDrive(GoogleJsonResponseException e) {
        int status = e.getStatusCode();
        String mensagem = e.getDetails() != null && e.getDetails().getMessage() != null
                ? e.getDetails().getMessage()
                : e.getMessage();

        if (status == 403 && mensagem != null && mensagem.contains("storageQuotaExceeded")) {
            return new IllegalStateException(
                    "Google Drive recusou o upload por quota. Use Shared Drive e configure "
                            + "uma pasta pai desse Shared Drive no módulo Google Drive.",
                    e
            );
        }

        return new IllegalStateException("Falha ao enviar arquivo para o Google Drive: " + mensagem, e);
    }
}
