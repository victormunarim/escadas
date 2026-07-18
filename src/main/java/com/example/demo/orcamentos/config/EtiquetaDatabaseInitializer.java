package com.example.demo.orcamentos.config;

import com.example.demo.orcamentos.model.EtiquetaEntity;
import com.example.demo.orcamentos.repository.EtiquetaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EtiquetaDatabaseInitializer implements CommandLineRunner {

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaDatabaseInitializer(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    @Override
    public void run(String... args) {
        List<String> etiquetasPadrao = List.of("Riscar", "Aguardando aprovação", "3D / Renderizar", "Proposta");
        for (String nome : etiquetasPadrao) {
            etiquetaRepository.findByNomeIgnoreCase(nome).orElseGet(() -> etiquetaRepository.save(new EtiquetaEntity(nome)));
        }
    }
}
