package com.example.demo.localidades.repository;

import com.example.demo.localidades.model.BairroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BairroRepository extends JpaRepository<BairroEntity, Integer> {
    List<BairroEntity> findDistinctByEstadoIdOrderByNomeAsc(Integer estadoId);
    Optional<BairroEntity> findByNomeIgnoreCaseAndMunicipioId(String nome, Integer municipioId);
}