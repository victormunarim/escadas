package com.example.demo.repository;

import com.example.demo.entity.BairroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BairroRepository extends JpaRepository<BairroEntity, Integer> {
    List<BairroEntity> findDistinctByEstadoIdOrderByNomeAsc(Integer estadoId);
    List<BairroEntity> findDistinctByMunicipioIdOrderByNomeAsc(Integer municipioId);
    java.util.Optional<BairroEntity> findByNomeIgnoreCaseAndMunicipioId(String nome, Integer municipioId);
}
