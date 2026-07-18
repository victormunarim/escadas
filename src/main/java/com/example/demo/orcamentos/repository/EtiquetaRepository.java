package com.example.demo.orcamentos.repository;

import com.example.demo.orcamentos.model.EtiquetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EtiquetaRepository extends JpaRepository<EtiquetaEntity, Long> {
    Optional<EtiquetaEntity> findByNomeIgnoreCase(String nome);
}
