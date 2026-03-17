package com.example.demo.service;

import com.example.demo.dto.ArquivoPedidoDTO;
import com.example.demo.entity.ArquivoPedidoEntity;
import com.example.demo.entity.CredenciaisDriveEntity;
import com.example.demo.entity.PedidoEntity;
import com.example.demo.repository.ArquivoPedidoRepository;
import com.example.demo.util.FormatacaoUtil;
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
public class ArquivoPedidoService {

    private final ArquivoPedidoRepository repositorioArquivoPedido;
    private final TokenDriveService servicoTokenDrive;
    private final CredenciaisDriveService servicoCredenciaisDrive;

    public ArquivoPedidoService(
            ArquivoPedidoRepository repositorioArquivoPedido,
            TokenDriveService servicoTokenDrive,
            CredenciaisDriveService servicoCredenciaisDrive
    ) {
        this.repositorioArquivoPedido = repositorioArquivoPedido;
        this.servicoTokenDrive = servicoTokenDrive;
        this.servicoCredenciaisDrive = servicoCredenciaisDrive;
    }

    public List<ArquivoPedidoDTO> listarPorPedido(Long pedidoId) {
        return repositorioArquivoPedido.findByPedidoIdOrderByNomeAsc(pedidoId).stream()
                .map(ArquivoPedidoDTO::new)
                .toList();
    }

    @Transactional
    public void enviarERegistrar(PedidoEntity pedido, MultipartFile multipartFile) {
        validarConfiguracao();

        String pastaPaiId = obterPastaPaiObrigatoria();
        String nomeArquivo = nomeArquivo(multipartFile);

        try {
            Drive drive = clienteDrive();
            String pastaPedidoId = obterOuCriarPastaPedido(drive, pedido, pastaPaiId);
            String linkArquivo = enviarArquivoParaDrive(drive, pastaPedidoId, nomeArquivo, multipartFile);

            ArquivoPedidoDTO arquivoDTO = new ArquivoPedidoDTO();
            arquivoDTO.setNome(nomeArquivo);
            arquivoDTO.setPedidoId(pedido.getId());
            arquivoDTO.setLink(linkArquivo);

            ArquivoPedidoEntity arquivoPedido = new ArquivoPedidoEntity(arquivoDTO);
            ArquivoPedidoEntity salvo = repositorioArquivoPedido.save(arquivoPedido);
            new ArquivoPedidoDTO(salvo);
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

    private String obterOuCriarPastaPedido(Drive drive, PedidoEntity pedido, String pastaPaiId) throws IOException {
        String nomePasta = FormatacaoUtil.nomePastaPedido(pedido.getNomeCliente(), pedido.getNumeroPedido());

        Optional<String> pastaExistente = buscarPastaPedido(drive, nomePasta, pastaPaiId);
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

    private Optional<String> buscarPastaPedido(Drive drive, String nomePasta, String pastaPaiId) throws IOException {
        StringBuilder consulta = new StringBuilder();
        consulta.append("mimeType = 'application/vnd.google-apps.folder' ");
        consulta.append("and trashed = false ");
        consulta.append("and name = '").append(nomePasta.replace("'", "\\'")).append("'");
        consulta.append(" and '").append(pastaPaiId.trim()).append("' in parents");

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
            erros.add("Credenciais do Google Drive não configuradas. Preencha client_id, project_id, client_secret e parent_folder.");
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
                    "Google Drive recusou o upload por quota. Use Shared Drive e configure uma pasta pai desse Shared Drive no módulo Google Drive.",
                    e
            );
        }

        return new IllegalStateException("Falha ao enviar arquivo para o Google Drive: " + mensagem, e);
    }
}
