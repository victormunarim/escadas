package com.example.demo.service;

import com.example.demo.entity.BairroEntity;
import com.example.demo.entity.EstadoEntity;
import com.example.demo.entity.MunicipioEntity;
import com.example.demo.repository.BairroRepository;
import com.example.demo.repository.EstadoRepository;
import com.example.demo.repository.MunicipioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class ConsultaLocalidadesService {
    private static final Logger log = LoggerFactory.getLogger(ConsultaLocalidadesService.class);

    private final BairroRepository repositorioBairro;
    private final MunicipioRepository repositorioMunicipio;
    private final EstadoRepository repositorioEstado;

    public ConsultaLocalidadesService(
            BairroRepository repositorioBairro,
            MunicipioRepository repositorioMunicipio,
            EstadoRepository repositorioEstado
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
                    repositorioBairro.findDistinctByUfIgnoreCaseOrderByNomeAsc(ufNormalizada).stream()
                            .map(BairroEntity::getNome),
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
                    repositorioMunicipio.findDistinctByUfIgnoreCaseOrderByNomeAsc(ufNormalizada).stream()
                            .map(MunicipioEntity::getNome),
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
                    .map(EstadoEntity::getUf)
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
