package com.example.demo.pedidos.localidade.service;

import com.example.demo.pedidos.localidade.model.Bairro;
import com.example.demo.pedidos.localidade.model.Estado;
import com.example.demo.pedidos.localidade.model.LocalidadeId;
import com.example.demo.pedidos.localidade.model.Municipio;
import com.example.demo.pedidos.localidade.repository.RepositorioBairro;
import com.example.demo.pedidos.localidade.repository.RepositorioEstado;
import com.example.demo.pedidos.localidade.repository.RepositorioMunicipio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Component
public class ConsultaLocalidades {
    private static final Logger log = LoggerFactory.getLogger(ConsultaLocalidades.class);

    private final RepositorioBairro repositorioBairro;
    private final RepositorioMunicipio repositorioMunicipio;
    private final RepositorioEstado repositorioEstado;

    public ConsultaLocalidades(
            RepositorioBairro repositorioBairro,
            RepositorioMunicipio repositorioMunicipio,
            RepositorioEstado repositorioEstado
    ) {
        this.repositorioBairro = repositorioBairro;
        this.repositorioMunicipio = repositorioMunicipio;
        this.repositorioEstado = repositorioEstado;
    }

    public List<String> buscarBairros(String termo, String uf, int limite) {
        String ufNormalizada = normalizarUf(uf);
        int limiteSeguro = limitar(limite);
        String termoNormalizado = normalizarParaBusca(termo);

        try {
            return filtrarNomes(
                    repositorioBairro.findDistinctByIdUfIgnoreCaseOrderByIdNomeAsc(ufNormalizada).stream()
                            .map(Bairro::getId)
                            .filter(id -> id != null)
                            .map(LocalidadeId::getNome),
                    termoNormalizado,
                    limiteSeguro
            );
        } catch (RuntimeException erro) {
            log.warn("Falha na busca de bairros para termo='{}' e uf='{}': {}", termo, ufNormalizada, erro.getMessage());
            return List.of();
        }
    }

    public List<String> buscarMunicipios(String termo, String uf, int limite) {
        String ufNormalizada = normalizarUf(uf);
        int limiteSeguro = limitar(limite);
        String termoNormalizado = normalizarParaBusca(termo);

        try {
            return filtrarNomes(
                    repositorioMunicipio.findDistinctByIdUfIgnoreCaseOrderByIdNomeAsc(ufNormalizada).stream()
                            .map(Municipio::getId)
                            .filter(id -> id != null)
                            .map(LocalidadeId::getNome),
                    termoNormalizado,
                    limiteSeguro
            );
        } catch (RuntimeException erro) {
            log.warn("Falha na busca de municipios para termo='{}' e uf='{}': {}", termo, ufNormalizada, erro.getMessage());
            return List.of();
        }
    }

    public List<String> buscarUfs() {
        try {
            return repositorioEstado.findDistinctByUfIsNotNullOrderByUfAsc().stream()
                    .map(Estado::getUf)
                    .map(this::normalizarUf)
                    .filter(valor -> !valor.isEmpty())
                    .toList();
        } catch (RuntimeException erro) {
            log.warn("Falha na busca de UFs: {}", erro.getMessage());
            return List.of("SC");
        }
    }

    private List<String> filtrarNomes(Stream<String> nomes, String termoNormalizado, int limite) {
        return nomes
                .map(valor -> valor == null ? "" : valor.trim())
                .filter(valor -> !valor.isEmpty())
                .filter(valor -> termoNormalizado.isEmpty() || normalizarParaBusca(valor).contains(termoNormalizado))
                .distinct()
                .limit(limite)
                .toList();
    }

    private int limitar(int limite) {
        return Math.max(1, Math.min(limite, 500));
    }

    private String normalizarUf(String uf) {
        if (uf == null) {
            return "SC";
        }
        String valor = uf.trim().toUpperCase(Locale.ROOT);
        return valor.isEmpty() ? "SC" : valor;
    }

    private String normalizarParaBusca(String valor) {
        if (valor == null || valor.isBlank()) {
            return "";
        }
        String semAcento = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return semAcento.toLowerCase(Locale.ROOT).trim();
    }
}
