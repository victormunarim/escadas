package com.example.demo.localidades.repository;
import com.example.demo.localidades.model.BairroEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BairroRepository extends JpaRepository<BairroEntity, Integer> {
    List<BairroEntity> findDistinctByEstadoIdOrderByNomeAsc(Integer estadoId);
    java.util.Optional<BairroEntity> findByNomeIgnoreCaseAndMunicipioId(String nome, Integer municipioId);
}